package ltd.hlaeja.util

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import ltd.hlaeja.dto.TypeWithDescription
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.jwt.service.PrivateJwtService
import ltd.hlaeja.library.deviceRegistry.Node
import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.web.server.ResponseStatusException

class MappingKtTest {
    companion object {
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    @BeforeEach
    fun setUp() {
        mockkStatic(ZonedDateTime::class)
        every { ZonedDateTime.now() } returns timestamp
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ZonedDateTime::class)
    }

    @Nested
    inner class TypeMapping {

        @Test
        fun `request to type entity successful`() {
            // given
            val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
            val request = Type.Request(
                "name",
                "description",
            )

            // when
            val entity = request.toTypeEntity(id)

            // then
            assertThat(entity.id).isEqualToUuid("00000000-0000-0000-0000-000000000001")
            assertThat(entity.timestamp).isEqualTo(timestamp)
            assertThat(entity.name).isEqualTo("name")
        }

        @Test
        fun `request to type description entity successful`() {
            // given
            val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
            val request = Type.Request(
                "name",
                "description",
            )

            // when
            val entity = request.toTypeDescriptionEntity(id)

            // then
            assertThat(entity.typeId).isEqualToUuid("00000000-0000-0000-0000-000000000001")
            assertThat(entity.description).isEqualTo("description")
        }

        @Test
        fun `type with description to response successful`() {
            // given
            val typeWithDescription = TypeWithDescription(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                timestamp,
                "name",
                "description",
            )

            // when
            val response = typeWithDescription.toTypeResponse()

            // then
            assertThat(response.id).isEqualToUuid("00000000-0000-0000-0000-000000000001")
            assertThat(response.timestamp).isEqualTo(timestamp)
            assertThat(response.name).isEqualTo("name")
            assertThat(response.description).isEqualTo("description")
        }

        @Test
        fun `type with description to response, description null successful`() {
            // given
            val typeWithDescription = TypeWithDescription(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                timestamp,
                "name",
                null,
            )

            // when
            val response = typeWithDescription.toTypeResponse()

            // then
            assertThat(response.id).isEqualToUuid("00000000-0000-0000-0000-000000000001")
            assertThat(response.timestamp).isEqualTo(timestamp)
            assertThat(response.name).isEqualTo("name")
            assertThat(response.description).isEmpty()
        }

        @Test
        fun `type entity to response successful`() {
            // given
            val entity = TypeEntity(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                timestamp,
                "name",
            )

            // when
            val response = entity.toTypesResponse()

            // then
            assertThat(response.id).isEqualToUuid("00000000-0000-0000-0000-000000000000")
            assertThat(response.timestamp).isEqualTo(timestamp)
            assertThat(response.name).isEqualTo("name")
        }
    }

    @Nested
    inner class NodeMapping {

        @Test
        fun `request to entity successful`() {
            // given
            val request = Node.Request(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "test",
            )

            // when
            val result = request.toEntity()

            // then
            assertThat(result.id).isNull()
            assertThat(result.timestamp).isEqualTo(timestamp)
            assertThat(result.client.toString()).isEqualTo("00000000-0000-0000-0000-000000000001")
            assertThat(result.device.toString()).isEqualTo("00000000-0000-0000-0000-000000000002")
            assertThat(result.name).isEqualTo("test")
        }

        @Test
        fun `entity to response successful`() {
            // given
            val entity = NodeEntity(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                timestamp,
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "test",
            )

            // when
            val result = entity.toNodeResponse()

            // then
            assertThat(result.id).isEqualToUuid("00000000-0000-0000-0000-000000000001")
            assertThat(result.client).isEqualToUuid("00000000-0000-0000-0000-000000000002")
            assertThat(result.device).isEqualToUuid("00000000-0000-0000-0000-000000000003")
            assertThat(result.name).isEqualTo("test")
        }

        @Test
        fun `entity to response exception`() {
            // given
            val entity = NodeEntity(
                null,
                timestamp,
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "test",
            )

            // then exception
            val exception = assertThrows(ResponseStatusException::class.java) {
                entity.toNodeResponse()
            }

            // then
            assertThat(exception.message).isEqualTo("417 EXPECTATION_FAILED")
        }
    }

    @Nested
    inner class IdentityMapping {

        @Test
        fun `entity to identity response successful`() {
            // given
            val entity = NodeEntity(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                timestamp,
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "test",
            )

            // when
            val result = entity.toIdentityResponse()

            // then
            assertThat(result.node).isEqualToUuid("00000000-0000-0000-0000-000000000001")
            assertThat(result.client).isEqualToUuid("00000000-0000-0000-0000-000000000002")
            assertThat(result.device).isEqualToUuid("00000000-0000-0000-0000-000000000003")
        }

        @Test
        fun `entity to identity response exception`() {
            // given
            val entity = NodeEntity(
                null,
                timestamp,
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                "test",
            )

            // then exception
            val exception = assertThrows(ResponseStatusException::class.java) {
                entity.toIdentityResponse()
            }

            // then
            assertThat(exception.message).isEqualTo("417 EXPECTATION_FAILED")
        }
    }

    @Nested
    inner class DeviceMapping {

        val jwtService: PrivateJwtService = mockk()

        @Test
        fun `entity to identity response successful`() {
            // given
            val entity = DeviceEntity(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                timestamp,
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
            )

            every { jwtService.sign(any()) } returns "header.payload.signature"

            // when
            val result = entity.toDeviceResponse(jwtService)

            // then
            assertThat(result.id).isEqualToUuid("00000000-0000-0000-0000-000000000001")
            assertThat(result.type).isEqualToUuid("00000000-0000-0000-0000-000000000002")
            assertThat(result.identity).isEqualTo("header.payload.signature")
        }

        @Test
        fun `entity to identity response exception`() {
            // given
            val entity = DeviceEntity(
                null,
                timestamp,
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
            )

            // then exception
            val exception = assertThrows(ResponseStatusException::class.java) {
                entity.toDeviceResponse(jwtService)
            }

            // then
            assertThat(exception.message).isEqualTo("417 EXPECTATION_FAILED")
        }
    }
}
