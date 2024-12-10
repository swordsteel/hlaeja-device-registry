package ltd.hlaeja.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.repository.DeviceRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.server.ResponseStatusException

class DeviceServiceTest {
    companion object {
        val timestamp = ZonedDateTime.ofInstant(Instant.parse("2000-01-01T00:00:00.001Z"), ZoneId.of("UTC"))
        val device = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val type = UUID.fromString("00000000-0000-0000-0000-000000000002")
    }

    val repository: DeviceRepository = mockk()
    lateinit var service: DeviceService

    @BeforeEach
    fun setUp() {
        service = DeviceService(repository)

        mockkStatic(ZonedDateTime::class)
        every { ZonedDateTime.now() } returns timestamp
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ZonedDateTime::class)
    }

    @Test
    fun `add new device success`() = runTest {
        // given
        coEvery { repository.save(any()) } answers { call ->
            (call.invocation.args[0] as DeviceEntity).copy(id = device)
        }

        // when
        val result = service.addDevice(type)

        // then
        coVerify(exactly = 1) { repository.save(any()) }

        assertThat(result.id).isEqualTo(device)
        assertThat(result.timestamp.toString()).isEqualTo("2000-01-01T00:00:00.001Z[UTC]")
        assertThat(result.type).isEqualTo(type)
    }

    @Test
    fun `get device - success`() = runTest {
        // given
        val device = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val entity: DeviceEntity = mockk()

        coEvery { repository.findById(any()) } returns entity
        coEvery { entity.id } returns device

        // when
        val result = service.getDevice(type)

        // then
        coVerify(exactly = 1) { repository.findById(any()) }
        assertThat(result.id).isEqualTo(device)
    }

    @Test
    fun `get device - fail not found`() = runTest {
        // given
        val device = UUID.fromString("00000000-0000-0000-0000-000000000000")

        coEvery { repository.findById(any()) } returns null

        // when
        val exception = assertThrows<ResponseStatusException> {
            service.getDevice(device)
        }

        // then
        assertThat(exception.message).isEqualTo("404 NOT_FOUND")
    }
}
