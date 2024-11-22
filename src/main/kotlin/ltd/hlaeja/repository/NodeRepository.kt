package ltd.hlaeja.repository

import java.util.UUID
import ltd.hlaeja.entity.NodeEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NodeRepository : CoroutineCrudRepository<NodeEntity, UUID>
