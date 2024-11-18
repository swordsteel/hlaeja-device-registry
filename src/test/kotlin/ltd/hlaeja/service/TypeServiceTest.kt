package ltd.hlaeja.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.repository.TypeRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TypeServiceTest {

    val repository: TypeRepository = mockk()

    lateinit var service: TypeService

    @BeforeEach
    fun setUp() {
        service = TypeService(repository)
    }

    @Test
    fun `get all types`() {
        // given
        every { repository.findAll() } returns flowOf(mockk<TypeEntity>())

        // when
        service.getTypes()

        // then
        verify(exactly = 1) { repository.findAll() }
    }
}
