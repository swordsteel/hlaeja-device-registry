package ltd.hlaeja.controller

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.dto.TypeWithDescription
import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.service.TypeService
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TypeControllerTest {
    companion object {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val timestamp = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    val service: TypeService = mockk()

    lateinit var controller: TypeController

    @BeforeEach
    fun setUp() {
        controller = TypeController(service)
    }

    @Test
    fun `add type`() = runTest {
        // given
        val request = Type.Request("name", "description")
        coEvery { service.addType(any(), any()) } returns TypeWithDescription(id, timestamp, "name", "description")

        // when
        val response = controller.addType(request)

        // then
        coVerify(exactly = 1) { service.addType(any(), any()) }

        assertThat(response.id).isEqualToUuid("00000000-0000-0000-0000-000000000000")
        assertThat(response.name).isEqualTo("name")
        assertThat(response.description).isEqualTo("description")
    }

    @Test
    fun `get type`() = runTest {
        // given
        coEvery { service.getType(any()) } returns TypeWithDescription(id, timestamp, "name", "description")

        // when
        val response = controller.getType(id)

        // then
        coVerify(exactly = 1) { service.getType(any()) }

        assertThat(response.id).isEqualToUuid("00000000-0000-0000-0000-000000000000")
        assertThat(response.name).isEqualTo("name")
        assertThat(response.description).isEqualTo("description")
    }

    @Test
    fun `update type`() = runTest {
        // given
        val request = Type.Request("name", "description")

        coEvery { service.updateType(any(), any(), any()) } answers { call ->
            TypeWithDescription(
                id = call.invocation.args[0] as UUID,
                timestamp = timestamp,
                name = call.invocation.args[1] as String,
                description = call.invocation.args[2] as String,
            )
        }

        // when
        val response = controller.updateType(id, request)

        // then
        coVerify(exactly = 1) { service.updateType(any(), any(), any()) }

        assertThat(response.id).isEqualToUuid("00000000-0000-0000-0000-000000000000")
        assertThat(response.timestamp).isEqualTo(timestamp)
        assertThat(response.name).isEqualTo("name")
        assertThat(response.description).isEqualTo("description")
    }
}
