package ltd.hlaeja.controller

import jakarta.validation.constraints.Min
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ltd.hlaeja.library.deviceRegistry.Nodes
import ltd.hlaeja.service.NodeService
import ltd.hlaeja.util.Pagination.DEFAULT_PAGE
import ltd.hlaeja.util.Pagination.DEFAULT_SIZE
import ltd.hlaeja.util.toNodesResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class NodesController(
    private val service: NodeService,
) {

    @GetMapping(
        "/nodes",
        "/nodes/page-{page}",
        "/nodes/page-{page}/show-{show}",
    )
    suspend fun getNodes(
        @PathVariable(required = false) @Min(1) page: Int = DEFAULT_PAGE,
        @PathVariable(required = false) @Min(1) show: Int = DEFAULT_SIZE,
    ): Flow<Nodes.Response> = service.getNodes((page - 1) * show, show)
        .map { it.toNodesResponse() }
}
