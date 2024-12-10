package ltd.hlaeja.service

import java.util.UUID
import kotlinx.coroutines.test.runTest
import ltd.hlaeja.property.JwtProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JwtServiceTest {

    val property: JwtProperty = JwtProperty("cert/valid-private-key.pem")
    lateinit var service: JwtService

    @BeforeEach
    fun setUp() {
        service = JwtService(property)
    }

    @Test
    fun `should generate a JWT successfully with a valid private key`() = runTest {
        // given
        val deviceId = UUID.fromString("00000000-0000-0000-0000-000000000000")

        // when
        val jwt = service.makeIdentity(deviceId)

        // then
        assertThat(jwt).contains("eyJkZXZpY2UiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAifQ")
    }
}
