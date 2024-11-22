package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Node
import ltd.hlaeja.service.NodeService
import ltd.hlaeja.util.toEntity
import ltd.hlaeja.util.toNodeResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class NodeController(
    private val nodeService: NodeService,
) {

    @PostMapping("/node")
    suspend fun addNode(
        @RequestBody request: Node.Request,
    ): Node.Response = nodeService.addNode(request.toEntity()).toNodeResponse()
}
