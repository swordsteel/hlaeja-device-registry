package ltd.hlaeja.repository

import java.util.UUID
import ltd.hlaeja.entity.DeviceEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : CoroutineCrudRepository<DeviceEntity, UUID>
