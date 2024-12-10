package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.ZonedDateTime
import java.util.UUID
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.repository.DeviceRepository
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
    ): DeviceEntity = deviceRepository.save(DeviceEntity(null, ZonedDateTime.now(), type))
        .also { log.debug { "Added device ${it.id}" } }

    suspend fun getDevice(device: UUID): DeviceEntity = deviceRepository.findById(device)
        ?.also { log.debug { "Get device ${it.id}" } }
        ?: throw ResponseStatusException(NOT_FOUND)
}
