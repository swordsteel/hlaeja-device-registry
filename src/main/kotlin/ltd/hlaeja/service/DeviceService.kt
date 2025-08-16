package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.repository.DeviceRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository,
) {

    suspend fun addDevice(
        type: UUID,
    ): DeviceEntity = try {
        deviceRepository.save(DeviceEntity(null, ZonedDateTime.now(), type))
            .also { log.debug { "Added device ${it.id}" } }
    } catch (e: DataIntegrityViolationException) {
        log.warn { e.localizedMessage }
        throw ResponseStatusException(BAD_REQUEST)
    }

    suspend fun getDevice(device: UUID): DeviceEntity = deviceRepository.findById(device)
        ?.also { log.debug { "Get device ${it.id}" } }
        ?: throw ResponseStatusException(NOT_FOUND)

    suspend fun getDevices(
        page: Int,
        show: Int,
    ): Flow<DeviceEntity> = deviceRepository.findAll(page, show)
}
