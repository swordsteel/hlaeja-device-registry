package ltd.hlaeja.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.assertFailsWith
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.dto.TypeWithDescription
import ltd.hlaeja.entity.TypeDescriptionEntity
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.repository.TypeDescriptionRepository
import ltd.hlaeja.repository.TypeRepository
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.EXPECTATION_FAILED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.server.ResponseStatusException

class TypeServiceTest {

    companion object {
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
        val uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

    val typeRepository: TypeRepository = mockk()
    val typeDescriptionRepository: TypeDescriptionRepository = mockk()

    lateinit var service: TypeService

    @BeforeEach
    fun setUp() {
        service = TypeService(typeRepository, typeDescriptionRepository)

        mockkStatic(ZonedDateTime::class)
        every { ZonedDateTime.now() } returns timestamp
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ZonedDateTime::class)
    }

    @Test
    fun `get all types`() {
        // given
        every { typeRepository.findAll(any(), any()) } returns flowOf(mockk<TypeEntity>())

        // when
        service.getTypes(1, 10, null)

        // then
        verify(exactly = 1) { typeRepository.findAll(1, 10) }
        verify(exactly = 0) { typeRepository.findAllContaining(any(), any(), any()) }
    }

    @Test
    fun `get all types with filter`() {
        // given
        every { typeRepository.findAllContaining(any(), any(), any()) } returns flowOf(mockk<TypeEntity>())

        // when
        service.getTypes(1, 10, "abc")

        // then
        verify(exactly = 1) { typeRepository.findAllContaining("%abc%", 1, 10) }
        verify(exactly = 0) { typeRepository.findAll(any(), any()) }
    }

    @Test
    fun `add new type success`() = runTest {
        // given
        coEvery { typeRepository.save(any()) } answers { call ->
            (call.invocation.args[0] as TypeEntity).copy(id = uuid)
        }
        coEvery { typeDescriptionRepository.upsert(any(), any()) } answers { call ->
            TypeDescriptionEntity(
                typeId = call.invocation.args[0] as UUID,
                description = call.invocation.args[1] as String,
            )
        }

        // when
        val result = service.addType("name", "description")

        // then
        coVerify(exactly = 1) { typeRepository.save(any()) }
        coVerify(exactly = 1) { typeDescriptionRepository.upsert(any(), any()) }

        assertThat(result.id).isEqualToUuid("00000000-0000-0000-0000-000000000000")
        assertThat(result.timestamp).isEqualTo(timestamp)
        assertThat(result.name).isEqualTo("name")
        assertThat(result.description).isEqualTo("description")
    }

    @Test
    fun `add new type - fail this should never happen save not updating id`() = runTest {
        // given
        coEvery { typeRepository.save(any()) } answers { call -> call.invocation.args[0] as TypeEntity }

        // when exception
        val response: ResponseStatusException = assertFailsWith<ResponseStatusException> {
            service.addType("name", "description")
        }

        // then
        coVerify(exactly = 1) { typeRepository.save(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.upsert(any(), any()) }

        assertThat(response.statusCode).isEqualTo(EXPECTATION_FAILED)
    }

    @Test
    fun `add new type - fail duplicate key`() = runTest {
        // given
        coEvery { typeRepository.save(any()) } throws DuplicateKeyException("duplicate key")

        // when exception
        val response: ResponseStatusException = assertFailsWith<ResponseStatusException> {
            service.addType("name", "description")
        }

        // then
        coVerify(exactly = 1) { typeRepository.save(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.upsert(any(), any()) }

        assertThat(response.statusCode).isEqualTo(CONFLICT)
    }

    @Test
    fun `get type - success`() = runTest {
        // given
        coEvery { typeRepository.findTypeWithDescription(any()) } answers { call ->
            TypeWithDescription(
                id = call.invocation.args[0] as UUID,
                timestamp = timestamp,
                name = "name",
                description = "description",
            )
        }

        // when
        val result = service.getType(uuid)

        // then
        coVerify(exactly = 1) { typeRepository.findTypeWithDescription(any()) }

        assertThat(result.id).isEqualToUuid("00000000-0000-0000-0000-000000000000")
        assertThat(result.timestamp).isEqualTo(timestamp)
        assertThat(result.name).isEqualTo("name")
        assertThat(result.description).isEqualTo("description")
    }

    @Test
    fun `get type - fail`() = runTest {
        // given
        coEvery { typeRepository.findTypeWithDescription(any()) } returns null

        // when exception
        val response: ResponseStatusException = assertFailsWith<ResponseStatusException> {
            service.getType(uuid)
        }

        // then
        coVerify(exactly = 1) { typeRepository.findTypeWithDescription(any()) }

        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `update type - success`() = runTest {
        // given
        coEvery { typeRepository.findById(any()) } answers { call ->
            TypeEntity(
                id = call.invocation.args[0] as UUID,
                timestamp = timestamp,
                name = "name",
            )
        }
        coEvery { typeRepository.save(any()) } answers { call ->
            call.invocation.args[0] as TypeEntity
        }
        coEvery { typeDescriptionRepository.findById(any()) } answers { call ->
            TypeDescriptionEntity(
                typeId = call.invocation.args[0] as UUID,
                description = "description",
            )
        }
        coEvery { typeDescriptionRepository.save(any()) } answers { call ->
            call.invocation.args[0] as TypeDescriptionEntity
        }

        // when
        val result = service.updateType(uuid, "new-name", "new-description")

        // then
        coVerify(exactly = 1) { typeRepository.findById(any()) }
        coVerify(exactly = 1) { typeRepository.save(any()) }
        coVerify(exactly = 1) { typeDescriptionRepository.findById(any()) }
        coVerify(exactly = 1) { typeDescriptionRepository.save(any()) }

        assertThat(result.id).isEqualToUuid("00000000-0000-0000-0000-000000000000")
        assertThat(result.timestamp).isEqualTo(timestamp)
        assertThat(result.name).isEqualTo("new-name")
        assertThat(result.description).isEqualTo("new-description")
    }

    @Test
    fun `update type - success no change`() = runTest {
        // given
        coEvery { typeRepository.findById(any()) } answers { call ->
            TypeEntity(
                id = call.invocation.args[0] as UUID,
                timestamp = timestamp,
                name = "name",
            )
        }
        coEvery { typeDescriptionRepository.findById(any()) } answers { call ->
            TypeDescriptionEntity(
                typeId = call.invocation.args[0] as UUID,
                description = "description",
            )
        }

        // when exception
        val response: ResponseStatusException = assertFailsWith<ResponseStatusException> {
            service.updateType(uuid, "name", "description")
        }

        // then
        coVerify(exactly = 1) { typeRepository.findById(any()) }
        coVerify(exactly = 0) { typeRepository.save(any()) }
        coVerify(exactly = 1) { typeDescriptionRepository.findById(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.save(any()) }

        assertThat(response.statusCode).isEqualTo(ACCEPTED)
    }

    @Test
    fun `update type - fail type dont exist`() = runTest {
        // given
        coEvery { typeRepository.findById(any()) } returns null

        // when exception
        val response: ResponseStatusException = assertFailsWith<ResponseStatusException> {
            service.updateType(uuid, "name", "description")
        }

        // then
        coVerify(exactly = 1) { typeRepository.findById(any()) }
        coVerify(exactly = 0) { typeRepository.save(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.findById(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.save(any()) }

        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `update type - fail type description dont exist`() = runTest {
        // given
        coEvery { typeRepository.findById(any()) } answers { call ->
            TypeEntity(
                id = call.invocation.args[0] as UUID,
                timestamp = timestamp,
                name = "name",
            )
        }
        coEvery { typeDescriptionRepository.findById(any()) } returns null

        // when exception
        val response: ResponseStatusException = assertFailsWith<ResponseStatusException> {
            service.updateType(uuid, "name", "description")
        }

        // then
        coVerify(exactly = 1) { typeRepository.findById(any()) }
        coVerify(exactly = 0) { typeRepository.save(any()) }
        coVerify(exactly = 1) { typeDescriptionRepository.findById(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.save(any()) }

        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `update type - fail name already exists`() = runTest {
        // given
        coEvery { typeRepository.findById(any()) } answers { call ->
            TypeEntity(
                id = call.invocation.args[0] as UUID,
                timestamp = timestamp,
                name = "name",
            )
        }
        coEvery { typeRepository.save(any()) } throws DuplicateKeyException("duplicate key")

        // when exception
        val response: ResponseStatusException = assertFailsWith<ResponseStatusException> {
            service.updateType(uuid, "taken-name", "description")
        }

        // then
        coVerify(exactly = 1) { typeRepository.findById(any()) }
        coVerify(exactly = 1) { typeRepository.save(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.findById(any()) }
        coVerify(exactly = 0) { typeDescriptionRepository.save(any()) }

        assertThat(response.statusCode).isEqualTo(CONFLICT)
    }
}
