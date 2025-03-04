package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Device
import ltd.hlaeja.test.compareToFile
import ltd.hlaeja.test.container.PostgresContainer
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@PostgresContainer
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SoftAssertionsExtension::class)
class DeviceEndpoint {

    @InjectSoftAssertions
    lateinit var softly: SoftAssertions

    @LocalServerPort
    var port: Int = 0

    lateinit var webClient: WebTestClient

    @BeforeEach
    fun setup() {
        webClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Nested
    inner class GetDevice {

        @Test
        fun `get account - success valid uuid`() {
            // given
            val uuid = UUID.fromString("00000000-0000-0000-0002-000000000001")

            // when
            val result = webClient.get().uri("/device-$uuid").exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<Device.Response>()
                .consumeWith {
                    softly.assertThat(it.responseBody?.id).isEqualTo(uuid)
                    softly.assertThat(it.responseBody?.type).isEqualToUuid("00000000-0000-0000-0001-000000000001")
                    softly.assertThat(it.responseBody?.identity).compareToFile("identity/first-device.data")
                }
        }

        @Test
        fun `get account - fail non-existent uuid`() {
            // given
            val uuid = UUID.fromString("00000000-0000-0000-0002-000000000000")

            // when
            val result = webClient.get().uri("/device-$uuid").exchange()

            // then
            result.expectStatus().isNotFound
        }
    }

    @Nested
    inner class CreateDevice {

        @Test
        fun `added device - success`() {
            // given
            val uuid = UUID.fromString("00000000-0000-0000-0001-000000000003")
            val request = Device.Request(
                type = uuid,
            )

            // when
            val result = webClient.post().uri("/device").bodyValue(request).exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<Device.Response>()
                .consumeWith {
                    softly.assertThat(it.responseBody?.id?.version()).isEqualTo(7)
                    softly.assertThat(it.responseBody?.type).isEqualTo(uuid)
                }
        }
    }
}
