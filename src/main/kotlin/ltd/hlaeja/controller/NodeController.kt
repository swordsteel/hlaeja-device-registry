package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Node
import ltd.hlaeja.service.NodeService
import ltd.hlaeja.util.toEntity
import ltd.hlaeja.util.toNodeClientResponse
import ltd.hlaeja.util.toNodeResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/node/device-{device}")
    suspend fun getNodeFromDevice(
        @PathVariable device: UUID,
    ): Node.NodeClientResponse = nodeService.getNodeFromDevice(device).toNodeClientResponse()
}
