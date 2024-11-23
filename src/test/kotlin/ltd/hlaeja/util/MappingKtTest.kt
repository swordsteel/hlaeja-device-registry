package ltd.hlaeja.util

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import ltd.hlaeja.assertj.assertThat
import ltd.hlaeja.entity.TypeEntity
import ltd.hlaeja.library.deviceRegistry.Type
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.web.server.ResponseStatusException

class MappingKtTest {
    companion object {
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    @BeforeEach
    fun setUp() {
        mockkStatic(ZonedDateTime::class)
        every { ZonedDateTime.now() } returns timestamp
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ZonedDateTime::class)
    }

    @Nested
    inner class TypeMapping {

        @Test
        fun `request to entity successful`() {
            // given
            val request = Type.Request(
                "test",
            )

            // when
            val result = request.toTypeEntity()

            // then
            assertThat(result.id).isNull()
            assertThat(result.timestamp.toString()).isEqualTo("2000-01-01T00:00:00.000000001Z[UTC]")
            assertThat(result.name).isEqualTo("test")
        }

        @Test
        fun `entity to response successful`() {
            // given
            val entity = TypeEntity(
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                timestamp,
                "name",
            )

            // when
            val response = entity.toTypeResponse()

            // then
            assertThat(response.id).isUUID("00000000-0000-0000-0000-000000000000")
            assertThat(response.name).isEqualTo("name")
        }

        @Test
        fun `entity to response exception`() {
            // then exception
            assertThrows(ResponseStatusException::class.java) {
                TypeEntity(null, timestamp, "name").toTypeResponse()
            }
        }
    }
}
