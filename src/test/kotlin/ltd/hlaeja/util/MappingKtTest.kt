package ltd.hlaeja.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import ltd.hlaeja.assertj.assertThat
import ltd.hlaeja.entity.TypeEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.springframework.web.server.ResponseStatusException

class MappingKtTest {
    companion object {
        val timestamp: ZonedDateTime = ZonedDateTime.of(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 1), ZoneId.of("UTC"))
    }

    @Nested
    inner class TypeMapping {

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
