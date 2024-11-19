package ltd.hlaeja.controller

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.service.TypeService
import ltd.hlaeja.util.toTypeEntity
import ltd.hlaeja.util.toTypeResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TypeController(
    private val service: TypeService,
) {

    @GetMapping("/types")
    fun getTypes(): Flow<Type.Response> = service.getTypes().map { it.toTypeResponse() }

    @PostMapping("/type")
    suspend fun addType(
        @RequestBody register: Type.Request,
    ): Type.Response = service.addType(register.toTypeEntity()).toTypeResponse()
}
