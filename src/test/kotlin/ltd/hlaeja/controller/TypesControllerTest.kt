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
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TypesControllerTest {
    companion object {
        const val NAME: String = "name"
        const val NIL_UUID: String = "00000000-0000-0000-0000-000000000000"
        val id: UUID = UUID.fromString(NIL_UUID)
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    val service: TypeService = mockk()

    lateinit var controller: TypesController

    @BeforeEach
    fun setUp() {
        controller = TypesController(service)
    }

    @Test
    fun `get all types`() = runTest {
        // given
        every {
            service.getTypes(any(), any(), any())
        } returns flowOf(TypeEntity(id, timestamp, NAME))

        // when
        val response = controller.getTypes().single()

        // then
        verify(exactly = 1) { service.getTypes(0, 25, null) }

        assertThat(response.id).isEqualToUuid(NIL_UUID)
        assertThat(response.name).isEqualTo(NAME)
        assertThat(response.timestamp).isEqualTo(timestamp)
    }
}
