package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.dto.TypeWithDescription
import ltd.hlaeja.entity.TypeDescriptionEntity
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.repository.TypeDescriptionRepository
import ltd.hlaeja.repository.TypeRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.EXPECTATION_FAILED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class TypeService(
    private val typeRepository: TypeRepository,
    private val typeDescriptionRepository: TypeDescriptionRepository,
) {

    fun getTypes(
        page: Int,
        show: Int,
        filter: String?,
    ): Flow<TypeEntity> = when {
        !filter.isNullOrEmpty() -> typeRepository.findAllContaining("%$filter%", page, show)
        else -> typeRepository.findAll(page, show)
    }

    @Transactional
    suspend fun addType(
        name: String,
        description: String,
    ): TypeWithDescription = try {
        val savedType = typeRepository.save(
            TypeEntity(timestamp = ZonedDateTime.now(), name = name),
        ).also { log.debug { "Added new type: ${it.id}" } }
        val savedDescription = typeDescriptionRepository.upsert(
            savedType.id ?: throw ResponseStatusException(EXPECTATION_FAILED),
            description,
        ).also { log.debug { "Added description for type: ${it.typeId}" } }
        TypeWithDescription(
            id = savedType.id,
            timestamp = savedType.timestamp,
            name = savedType.name,
            description = savedDescription.description,
        )
    } catch (e: DataIntegrityViolationException) {
        log.warn { "Failed to add type with name '$name': ${e.localizedMessage}" }
        throw ResponseStatusException(CONFLICT, "Type with name '$name' already exists")
    }

    suspend fun getType(
        id: UUID,
    ): TypeWithDescription = typeRepository.findTypeWithDescription(id)
        ?.also { log.debug { "Retrieved type with description: ${it.id}" } }
        ?: throw ResponseStatusException(NOT_FOUND, "Type with id '$id' not found")

    @Transactional
    suspend fun updateType(
        id: UUID,
        name: String,
        description: String,
    ): TypeWithDescription {
        var hasChanges = false
        val updatedType = updateType(id, name) { hasChanges = true }
        val updatedTypeDescription = updateTypeDescription(id, description) { hasChanges = true }
        if (!hasChanges) {
            throw ResponseStatusException(ACCEPTED, "No changes for type with id '$id'")
        }
        return TypeWithDescription(
            id = updatedType.id!!,
            timestamp = updatedType.timestamp,
            name = updatedType.name,
            description = updatedTypeDescription.description,
        )
    }

    private suspend fun updateTypeDescription(
        id: UUID,
        description: String,
        onChange: () -> Unit,
    ): TypeDescriptionEntity {
        val existingDescription = typeDescriptionRepository.findById(id)
            ?: throw ResponseStatusException(NOT_FOUND, "Type description with id '$id' not found")
        return if (existingDescription.description == description) {
            existingDescription
        } else {
            onChange()
            typeDescriptionRepository.save(existingDescription.copy(description = description))
        }
    }

    private suspend fun updateType(
        id: UUID,
        name: String,
        onChange: () -> Unit,
    ): TypeEntity {
        val existingType = typeRepository.findById(id)
            ?: throw ResponseStatusException(NOT_FOUND, "Type with id '$id' not found")
        return if (existingType.name == name) {
            existingType
        } else {
            onChange()
            try {
                typeRepository.save(existingType.copy(name = name))
            } catch (e: DataIntegrityViolationException) {
                log.warn { "Failed to update type with name '$name': ${e.localizedMessage}" }
                throw ResponseStatusException(CONFLICT, "Type with name '$name' already exists")
            }
        }
    }
}
