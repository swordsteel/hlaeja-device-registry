package ltd.hlaeja.repository

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.entity.DeviceEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : CoroutineCrudRepository<DeviceEntity, UUID> {

    @Query("SELECT * FROM devices LIMIT :limit OFFSET :offset")
    fun findAll(
        @Param("offset") offset: Int,
        @Param("limit") limit: Int,
    ): Flow<DeviceEntity>
}
