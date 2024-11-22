package ltd.hlaeja.controller

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.UUID
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.assertj.assertThat
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.library.deviceRegistry.Node
import ltd.hlaeja.service.NodeService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NodeControllerTest {

    val service: NodeService = mockk()

    lateinit var controller: NodeController

    @BeforeEach
    fun setUp() {
        controller = NodeController(service)
    }

    @Test
    fun `add device - success`() = runTest {
        // given
        val request: Node.Request = Node.Request(
            UUID.fromString("00000000-0000-0000-0000-000000000001"),
            UUID.fromString("00000000-0000-0000-0000-000000000002"),
            "test",
        )
        coEvery { service.addNode(any()) } answers { call ->
            (call.invocation.args[0] as NodeEntity).copy(id = UUID.fromString("00000000-0000-0000-0000-000000000003"))
        }

        // when
        val response = controller.addNode(request)

        // then
        coVerify(exactly = 1) { service.addNode(any()) }

        assertThat(response.id).isUUID("00000000-0000-0000-0000-000000000003")
        assertThat(response.client).isUUID("00000000-0000-0000-0000-000000000001")
        assertThat(response.device).isUUID("00000000-0000-0000-0000-000000000002")
        assertThat(response.name).isEqualTo("test")
    }
}
