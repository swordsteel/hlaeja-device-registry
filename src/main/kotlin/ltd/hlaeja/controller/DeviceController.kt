package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.jwt.service.PrivateJwtService
import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.service.DeviceService
import ltd.hlaeja.util.toDeviceResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DeviceController(
    private val deviceService: DeviceService,
    private val privateJwtService: PrivateJwtService,
) {

    @PostMapping("/device")
    suspend fun addDevice(
        @RequestBody request: Device.Request,
    ): Device.Response = deviceService.addDevice(request.type)
        .toDeviceResponse(privateJwtService)

    @GetMapping("/device-{device}")
    suspend fun getDevice(
        @PathVariable device: UUID,
    ): Device.Response = deviceService.getDevice(device)
        .toDeviceResponse(privateJwtService)
}
