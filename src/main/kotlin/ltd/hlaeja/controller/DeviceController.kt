package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Device
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
        @RequestBody request: Device.Request,
    ): Device.Identity = deviceService.addDevice(request.type)
        .let { jwtService.makeIdentity(it.id ?: throw ResponseStatusException(EXPECTATION_FAILED)) }
        .let { Device.Identity(it) }
}
