package ltd.hlaeja.repository

import java.util.UUID
import ltd.hlaeja.entity.TypeDescriptionEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TypeDescriptionRepository : CoroutineCrudRepository<TypeDescriptionEntity, UUID> {
    @Query(
        """
            INSERT INTO type_descriptions (type_id, description) VALUES (:type_id, :description)
            ON CONFLICT (type_id)
            DO UPDATE SET description = :description
            RETURNING *
        """,
    )
    suspend fun upsert(
        @Param("type_id") typeId: UUID,
        @Param("description") description: String,
    ): TypeDescriptionEntity
}
