package ltd.hlaeja.controller

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.service.NodeService
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdentityControllerTest {
    companion object {
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    val service: NodeService = mockk()

    lateinit var controller: IdentityController

    @BeforeEach
    fun setUp() {
        controller = IdentityController(service)
    }

    @Test
    fun `get node by device - success`() = runTest {
        // given
        val device = UUID.fromString("00000000-0000-0000-0000-000000000003")
        val entity = NodeEntity(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            timestamp,
            UUID.fromString("00000000-0000-0000-0000-000000000002"),
            device,
            "test",
        )

        coEvery { service.getNodeFromDevice(any()) } returns entity

        // when
        val response = controller.getIdentityFromDevice(device)

        // then
        coVerify(exactly = 1) { service.getNodeFromDevice(any()) }

        assertThat(response.node).isEqualToUuid("00000000-0000-0000-0000-000000000001")
        assertThat(response.client).isEqualToUuid("00000000-0000-0000-0000-000000000002")
        assertThat(response.device).isEqualToUuid("00000000-0000-0000-0000-000000000003")
    }
}
