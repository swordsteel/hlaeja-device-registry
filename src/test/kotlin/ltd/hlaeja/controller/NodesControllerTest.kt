package ltd.hlaeja.controller

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.service.NodeService
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NodesControllerTest {
    companion object {
        const val NAME: String = "My Device"
        const val NIL_UUID: String = "00000000-0000-0000-0000-000000000000"
        val id: UUID = UUID.fromString(NIL_UUID)
        val client: UUID = UUID.fromString(NIL_UUID)
        val device: UUID = UUID.fromString(NIL_UUID)
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    val service: NodeService = mockk()

    lateinit var controller: NodesController

    @BeforeEach
    fun setUp() {
        controller = NodesController(service)
    }

    @Test
    fun `get all nodes`() = runTest {
        // given
        coEvery {
            service.getNodes(any(), any())
        } returns flowOf(NodeEntity(id, timestamp, client, device, NAME))

        // when
        val response = controller.getNodes().single()

        // then
        coVerify(exactly = 1) { service.getNodes(0, 25) }

        assertThat(response.id).isEqualToUuid(NIL_UUID)
        assertThat(response.timestamp).isEqualTo(timestamp)
        assertThat(response.client).isEqualToUuid(NIL_UUID)
        assertThat(response.device).isEqualToUuid(NIL_UUID)
        assertThat(response.name).isEqualTo(NAME)
    }
}
