package ltd.hlaeja.entity

import java.time.ZonedDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("nodes")
data class NodeEntity(
    @Id
    val id: UUID? = null,
    val timestamp: ZonedDateTime,
    val client: UUID,
    val device: UUID,
    val name: String,
)
