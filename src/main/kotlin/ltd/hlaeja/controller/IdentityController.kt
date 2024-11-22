package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.service.NodeService
import ltd.hlaeja.util.toIdentityResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/identity")
class IdentityController(
    private val nodeService: NodeService,
) {

    @GetMapping("/device-{device}")
    suspend fun getIdentityFromDevice(
        @PathVariable device: UUID,
    ): Identity.Response = nodeService.getNodeFromDevice(device).toIdentityResponse()
}
