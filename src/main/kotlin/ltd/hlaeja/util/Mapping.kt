package ltd.hlaeja.util

import java.time.ZonedDateTime
import java.util.UUID
import ltd.hlaeja.dto.TypeWithDescription
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.entity.TypeDescriptionEntity
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.jwt.service.PrivateJwtService
import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.library.deviceRegistry.Devices
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.library.deviceRegistry.Node
import ltd.hlaeja.library.deviceRegistry.Nodes
import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.library.deviceRegistry.Types
import org.springframework.http.HttpStatus.EXPECTATION_FAILED
import org.springframework.web.server.ResponseStatusException

fun Type.Request.toTypeEntity(id: UUID): TypeEntity = TypeEntity(
    id = id,
    timestamp = ZonedDateTime.now(),
    name = name,
)

fun Type.Request.toTypeDescriptionEntity(id: UUID): TypeDescriptionEntity = TypeDescriptionEntity(
    typeId = id,
    description = description,
)

fun TypeWithDescription.toTypeResponse(): Type.Response = Type.Response(
    id = id,
    timestamp = timestamp,
    name = name,
    description = description ?: "",
)

fun TypeEntity.toTypesResponse(): Types.Response = Types.Response(
    id = id!!,
    name = name,
    timestamp = timestamp,
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

fun DeviceEntity.toDeviceResponse(
    jwtService: PrivateJwtService,
): Device.Response = Device.Response(
    id ?: throw ResponseStatusException(EXPECTATION_FAILED),
    type,
    jwtService.sign("device" to id),
)

fun DeviceEntity.toDevicesResponse(): Devices.Response = Devices.Response(
    id ?: throw ResponseStatusException(EXPECTATION_FAILED),
    type,
    timestamp,
)

fun NodeEntity.toNodesResponse(): Nodes.Response = Nodes.Response(
    id ?: throw ResponseStatusException(EXPECTATION_FAILED),
    timestamp,
    client,
    device,
    name,
)
