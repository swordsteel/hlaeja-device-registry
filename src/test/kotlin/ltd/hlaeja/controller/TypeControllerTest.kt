package ltd.hlaeja.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.service.TypeService
import ltd.hlaeja.assertj.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TypeControllerTest {
    companion object {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val timestamp = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    val service: TypeService = mockk()

    lateinit var controller: TypeController

    @BeforeEach
    fun setUp() {
        controller = TypeController(service)
    }

    @Test
    fun `get all types`() = runTest {
        // given
        every { service.getTypes() } returns flowOf(TypeEntity(id, timestamp, "name"))

        // when
        val response = controller.getTypes().single()

        // then
        verify(exactly = 1) { service.getTypes() }

        assertThat(response.id).isUUID("00000000-0000-0000-0000-000000000000")
        assertThat(response.name).isEqualTo("name")
    }
}
