package ltd.hlaeja.entity

import java.time.ZonedDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("devices")
data class DeviceEntity(
    @Id
    val id: UUID? = null,
    val timestamp: ZonedDateTime,
    val type: UUID,
)
