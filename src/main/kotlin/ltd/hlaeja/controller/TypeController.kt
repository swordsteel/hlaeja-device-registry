package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.service.TypeService
import ltd.hlaeja.util.toTypeResponse
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class TypeController(
    private val service: TypeService,
) {

    @PostMapping("/type")
    @ResponseStatus(CREATED)
    suspend fun addType(
        @RequestBody request: Type.Request,
    ): Type.Response = service.addType(request.name, request.description).toTypeResponse()

    @GetMapping("/type-{type}")
    suspend fun getType(
        @PathVariable type: UUID,
    ): Type.Response = service.getType(type).toTypeResponse()

    @PutMapping("/type-{type}")
    suspend fun updateType(
        @PathVariable type: UUID,
        @RequestBody request: Type.Request,
    ): Type.Response = service.updateType(type, request.name, request.description).toTypeResponse()
}
