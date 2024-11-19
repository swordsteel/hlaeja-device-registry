package ltd.hlaeja.service

import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.repository.TypeRepository
import mu.KotlinLogging
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class TypeService(
    private val typeRepository: TypeRepository,
) {

    fun getTypes(): Flow<TypeEntity> = typeRepository.findAll()

    suspend fun addType(
        entity: TypeEntity,
    ): TypeEntity = try {
        typeRepository.save(entity)
            .also { log.debug("Added new type: {}", it.id) }
    } catch (e: DuplicateKeyException) {
        log.warn(e.localizedMessage)
        throw ResponseStatusException(HttpStatus.CONFLICT)
    }
}
