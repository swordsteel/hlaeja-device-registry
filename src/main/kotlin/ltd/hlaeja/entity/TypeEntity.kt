package ltd.hlaeja.entity

import java.time.ZonedDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("types")
data class TypeEntity(
    @Id
    val id: UUID? = null,
    val timestamp: ZonedDateTime,
    val name: String,
)
