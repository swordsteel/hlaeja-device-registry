package ltd.hlaeja.repository

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.entity.NodeEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NodeRepository : CoroutineCrudRepository<NodeEntity, UUID> {

    @Query("SELECT * FROM nodes WHERE device = :device")
    suspend fun findByDevice(device: UUID): NodeEntity?

    @Query("SELECT * FROM nodes LIMIT :limit OFFSET :offset")
    suspend fun findAll(
        @Param("offset") offset: Int,
        @Param("limit") limit: Int,
    ): Flow<NodeEntity>
}
