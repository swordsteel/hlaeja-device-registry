package ltd.hlaeja.repository

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.dto.TypeWithDescription
import ltd.hlaeja.entity.TypeEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TypeRepository : CoroutineCrudRepository<TypeEntity, UUID> {

    @Query("SELECT * FROM types ORDER BY name LIMIT :limit OFFSET :offset")
    suspend fun findAll(
        @Param("offset") offset: Int,
        @Param("limit") limit: Int,
    ): Flow<TypeEntity>

    @Query("SELECT * FROM types WHERE name ILIKE :filter ORDER BY name LIMIT :limit OFFSET :offset")
    suspend fun findAllContaining(
        @Param("filter") filter: String,
        @Param("offset") offset: Int,
        @Param("limit") limit: Int,
    ): Flow<TypeEntity>

    @Query(
        """
            SELECT t.id, t.timestamp, t.name, td.description
            FROM types t
            LEFT JOIN type_descriptions td ON t.id = td.type_id
            WHERE t.id = :id
        """,
    )
    suspend fun findTypeWithDescription(
        @Param("id") id: UUID,
    ): TypeWithDescription?
}
