package ltd.hlaeja.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.assertFailsWith
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.repository.TypeRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException
import org.springframework.web.server.ResponseStatusException

class TypeServiceTest {
    companion object {
        val timestamp = ZonedDateTime.ofInstant(Instant.parse("2000-01-01T00:00:00.001Z"), ZoneId.of("UTC"))
        val uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

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

    @Test
    fun `add new type success`() = runTest {
        // given
        val entity = TypeEntity(
            null,
            timestamp,
            "name",
        )

        coEvery { repository.save(any()) } answers { call -> (call.invocation.args[0] as TypeEntity).copy(id = uuid) }

        // when
        service.addType(entity)

        // then
        coVerify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `add new type exception`() = runTest {
        // given
        val entity: TypeEntity = mockk()

        coEvery { repository.save(any()) } throws DuplicateKeyException("duplicate key")

        // then exception
        assertFailsWith<ResponseStatusException> {
            service.addType(entity)
        }
    }
}
