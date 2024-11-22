package ltd.hlaeja.service

import java.util.UUID
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.repository.NodeRepository
import mu.KotlinLogging
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
    ): NodeEntity = nodeRepository.save(node)
        .also { log.debug { "Added node ${it.id}" } }

    suspend fun getNodeFromDevice(
        device: UUID,
    ): NodeEntity = nodeRepository.findByDevice(device)
        ?: throw ResponseStatusException(NOT_FOUND)
}
