package ltd.hlaeja.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.UUID
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.repository.NodeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.server.ResponseStatusException

class NodeServiceTest {

    val repository: NodeRepository = mockk(relaxed = true)
    lateinit var service: NodeService

    @BeforeEach
    fun setUp() {
        service = NodeService(repository)
    }

    @Test
    fun `add new node - success`() = runTest {
        // given
        val entity: NodeEntity = mockk()

        coEvery { repository.save(any()) } returns entity

        // when
        service.addNode(entity)

        // then
        coVerify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `get type - success`() = runTest {
        // given
        val device = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val entity: NodeEntity = mockk()

        coEvery { repository.findByDevice(any()) } returns entity

        // when
        service.getNodeFromDevice(device)

        // then
        coVerify(exactly = 1) { repository.findByDevice(any()) }
    }

    @Test
    fun `get type - fail not found`() = runTest {
        // given
        val device = UUID.fromString("00000000-0000-0000-0000-000000000001")

        coEvery { repository.findByDevice(any()) } returns null

        // when
        val exception = assertThrows<ResponseStatusException> {
            service.getNodeFromDevice(device)
        }

        // then
        assertThat(exception.message).isEqualTo("404 NOT_FOUND")
    }
}
