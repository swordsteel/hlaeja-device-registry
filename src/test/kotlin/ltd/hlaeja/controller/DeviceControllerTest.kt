package ltd.hlaeja.controller

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.DeviceEntity
import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.service.DeviceService
import ltd.hlaeja.service.JwtService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.server.ResponseStatusException

class DeviceControllerTest {
    companion object {
        const val PAYLOAD: String = "head.body.signature"

        val uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val timestamp = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    val deviceService: DeviceService = mockk()
    val jwtService: JwtService = mockk()

    lateinit var controller: DeviceController

    @BeforeEach
    fun setUp() {
        controller = DeviceController(deviceService, jwtService)
    }

    @Nested
    inner class AddDeviceTest {

        @Test
        fun `add device - success`() = runTest {
            // given
            val request = Device.Request(uuid)
            coEvery { deviceService.addDevice(any()) } returns DeviceEntity(uuid, timestamp, uuid)
            coEvery { jwtService.makeIdentity(any()) } returns PAYLOAD

            // when
            val response = controller.addDevice(request)

            // then
            coVerify(exactly = 1) { deviceService.addDevice(any()) }
            coVerify(exactly = 1) { jwtService.makeIdentity(any()) }

            assertThat(response.identity).isEqualTo(PAYLOAD)
        }

        @Test
        fun `add device - device service fail`() = runTest {
            // given
            val request = Device.Request(uuid)
            coEvery { deviceService.addDevice(any()) } returns DeviceEntity(null, timestamp, uuid)

            // when exception
            val exception = assertThrows<ResponseStatusException> {
                controller.addDevice(request)
            }

            // then
            assertThat(exception.message).isEqualTo("417 EXPECTATION_FAILED")
        }
    }

    @Nested
    inner class GetDeviceTest {

        @Test
        fun `get device - success`() = runTest {
            // given
            coEvery { deviceService.getDevice(any()) } returns DeviceEntity(uuid, timestamp, uuid)
            coEvery { jwtService.makeIdentity(any()) } returns PAYLOAD

            // when
            val response = controller.getDevice(uuid)

            // then
            coVerify(exactly = 1) { deviceService.getDevice(any()) }
            coVerify(exactly = 1) { jwtService.makeIdentity(any()) }

            assertThat(response.identity).isEqualTo(PAYLOAD)
        }
    }
}
