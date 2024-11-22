package ltd.hlaeja.util

import java.time.ZonedDateTime
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.library.deviceRegistry.Node
import ltd.hlaeja.library.deviceRegistry.Type
import org.springframework.http.HttpStatus.EXPECTATION_FAILED
import org.springframework.web.server.ResponseStatusException

fun Type.Request.toTypeEntity(): TypeEntity = TypeEntity(null, ZonedDateTime.now(), name)

fun TypeEntity.toTypeResponse(): Type.Response = Type.Response(
    id ?: throw ResponseStatusException(EXPECTATION_FAILED),
    name,
)

fun Node.Request.toEntity(): NodeEntity = NodeEntity(
    null,
    ZonedDateTime.now(),
    client,
    device,
    name,
)

fun NodeEntity.toNodeResponse(): Node.Response = Node.Response(
    id ?: throw ResponseStatusException(EXPECTATION_FAILED),
    client,
    device,
    name,
)

fun NodeEntity.toIdentityResponse(): Identity.Response = Identity.Response(
    client,
    id ?: throw ResponseStatusException(EXPECTATION_FAILED),
    device,
)
