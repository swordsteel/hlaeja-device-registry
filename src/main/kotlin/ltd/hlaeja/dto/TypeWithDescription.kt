package ltd.hlaeja.dto

import java.time.ZonedDateTime
import java.util.UUID

data class TypeWithDescription(
    val id: UUID,
    val timestamp: ZonedDateTime,
    val name: String,
    val description: String?,
)
