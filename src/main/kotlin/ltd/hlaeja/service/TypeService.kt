package ltd.hlaeja.service

import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.repository.TypeRepository
import org.springframework.stereotype.Service

@Service
class TypeService(
    private val typeRepository: TypeRepository,
) {

    fun getTypes(): Flow<TypeEntity> = typeRepository.findAll()
}
