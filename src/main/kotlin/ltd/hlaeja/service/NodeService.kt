package ltd.hlaeja.service

import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.repository.NodeRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class NodeService(
    private val nodeRepository: NodeRepository,
) {

    suspend fun addNode(
        node: NodeEntity,
    ): NodeEntity = nodeRepository.save(node)
        .also { log.debug { "Added node ${it.id}" } }
}
