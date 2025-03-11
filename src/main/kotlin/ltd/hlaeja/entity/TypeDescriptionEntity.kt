package ltd.hlaeja.entity

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("type_descriptions")
data class TypeDescriptionEntity(
    @Id
    val typeId: UUID,
    val description: String,
)
