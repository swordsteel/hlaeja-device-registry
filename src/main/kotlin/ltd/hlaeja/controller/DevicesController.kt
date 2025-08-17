package ltd.hlaeja.controller

import jakarta.validation.constraints.Min
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ltd.hlaeja.library.deviceRegistry.Devices
import ltd.hlaeja.service.DeviceService
import ltd.hlaeja.util.toDevicesResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DevicesController(
    private val deviceService: DeviceService,
) {

    companion object {
        const val DEFAULT_PAGE: Int = 1
        const val DEFAULT_SIZE: Int = 25
    }

    @GetMapping(
        "/devices",
        "/devices/page-{page}",
        "/devices/page-{page}/show-{show}",
    )
    suspend fun getDevices(
        @PathVariable(required = false) @Min(1) page: Int = DEFAULT_PAGE,
        @PathVariable(required = false) @Min(1) show: Int = DEFAULT_SIZE,
    ): Flow<Devices.Response> = deviceService.getDevices((page - 1) * show, show)
        .map { it.toDevicesResponse() }

    @GetMapping(
        "/devices/type-{type}",
        "/devices/type-{type}/page-{page}",
        "/devices/type-{type}/page-{page}/show-{show}",
    )
    suspend fun getDevicesByType(
        @PathVariable type: UUID,
        @PathVariable(required = false) @Min(1) page: Int = DEFAULT_PAGE,
        @PathVariable(required = false) @Min(1) show: Int = DEFAULT_SIZE,
    ): Flow<Devices.Response> = deviceService.getDevicesByType(type, (page - 1) * show, show)
        .map { it.toDevicesResponse() }
}
