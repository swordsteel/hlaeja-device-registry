package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.repository.NodeRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class NodeService(
    private val nodeRepository: NodeRepository,
) {

    suspend fun addNode(
        node: NodeEntity,
    ): NodeEntity = try {
        nodeRepository.save(node).also { log.debug { "Added node ${it.id}" } }
    } catch (exception: DataIntegrityViolationException) {
        throw ResponseStatusException(BAD_REQUEST, null, exception)
    }

    suspend fun getNodeFromDevice(
        device: UUID,
    ): NodeEntity = nodeRepository.findByDevice(device)
        ?: throw ResponseStatusException(NOT_FOUND)

    suspend fun getNodes(
        page: Int,
        show: Int,
    ): Flow<NodeEntity> = nodeRepository.findAll(page, show)
}
