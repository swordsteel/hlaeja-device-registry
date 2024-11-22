package ltd.hlaeja.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.NodeEntity
import ltd.hlaeja.repository.NodeRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NodeServiceTest {

    val repository: NodeRepository = mockk()
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
}
