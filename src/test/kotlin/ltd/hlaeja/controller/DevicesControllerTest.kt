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
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.service.DeviceService
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DevicesControllerTest {
    companion object {
        const val NIL_UUID: String = "00000000-0000-0000-0000-000000000000"
        val id: UUID = UUID.fromString(NIL_UUID)
        val type: UUID = UUID.fromString(NIL_UUID)
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    val service: DeviceService = mockk()

    lateinit var controller: DevicesController

    @BeforeEach
    fun setUp() {
        controller = DevicesController(service)
    }

    @Test
    fun `get all devices`() = runTest {
        // given
        coEvery {
            service.getDevices(any(), any())
        } returns flowOf(DeviceEntity(id, timestamp, type))

        // when
        val response = controller.getDevices().single()

        // then
        coVerify(exactly = 1) { service.getDevices(0, 25) }

        assertThat(response.id).isEqualToUuid(NIL_UUID)
        assertThat(response.type).isEqualToUuid(NIL_UUID)
        assertThat(response.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `get all devices for type`() = runTest {
        // given
        coEvery {
            service.getDevicesByType(any(), any(), any())
        } returns flowOf(DeviceEntity(id, timestamp, type))

        // when
        val response = controller.getDevicesByType(type).single()

        // then
        coVerify(exactly = 1) { service.getDevicesByType(type, 0, 25) }

        assertThat(response.id).isEqualToUuid(NIL_UUID)
        assertThat(response.type).isEqualToUuid(NIL_UUID)
        assertThat(response.timestamp).isEqualTo(timestamp)
    }
}
