package ltd.hlaeja.repository

import java.util.UUID
import ltd.hlaeja.entity.NodeEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NodeRepository : CoroutineCrudRepository<NodeEntity, UUID> {

    @Query("SELECT * FROM nodes WHERE device = :device")
    suspend fun findByDevice(device: UUID): NodeEntity?
}
