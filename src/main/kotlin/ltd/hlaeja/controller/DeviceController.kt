package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.service.DeviceService
import ltd.hlaeja.service.JwtService
import org.springframework.http.HttpStatus.EXPECTATION_FAILED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class DeviceController(
    private val deviceService: DeviceService,
    private val jwtService: JwtService,
) {

    @PostMapping("/device")
    suspend fun addDevice(
        @RequestBody request: Identity.Request,
    ): Identity.Response = deviceService.addDevice(request.type)
        .let { jwtService.makeIdentity(it.id ?: throw ResponseStatusException(EXPECTATION_FAILED)) }
        .let { Identity.Response(it) }
}
