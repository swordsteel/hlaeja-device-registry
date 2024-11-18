package ltd.hlaeja.repository

import java.util.UUID
import ltd.hlaeja.entity.TypeEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TypeRepository : CoroutineCrudRepository<TypeEntity, UUID>
