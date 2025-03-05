package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.service.TypeService
import ltd.hlaeja.util.toTypeEntity
import ltd.hlaeja.util.toTypeResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TypeController(
    private val service: TypeService,
) {

    @PostMapping("/type")
    suspend fun addType(
        @RequestBody register: Type.Request,
    ): Type.Response = service.addType(register.toTypeEntity()).toTypeResponse()
}
