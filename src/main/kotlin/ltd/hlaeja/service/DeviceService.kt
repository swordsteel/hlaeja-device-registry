package ltd.hlaeja.service

import java.time.ZonedDateTime
import java.util.UUID
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.repository.DeviceRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository,
) {

    suspend fun addDevice(
        type: UUID,
    ): DeviceEntity = deviceRepository.save(DeviceEntity(null, ZonedDateTime.now(), type))
        .also { log.debug { "Added device ${it.id}" } }
}
