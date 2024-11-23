package ltd.hlaeja.util

import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.library.deviceRegistry.Type
import org.springframework.http.HttpStatus.EXPECTATION_FAILED
import org.springframework.web.server.ResponseStatusException

fun TypeEntity.toTypeResponse(): Type.Response {
    return Type.Response(id ?: throw ResponseStatusException(EXPECTATION_FAILED), name)
}
