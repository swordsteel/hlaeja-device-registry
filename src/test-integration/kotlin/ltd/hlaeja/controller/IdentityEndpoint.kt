package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Identity
import ltd.hlaeja.test.container.PostgresTestContainer
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@PostgresTestContainer
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SoftAssertionsExtension::class)
class IdentityEndpoint {

    @InjectSoftAssertions
    lateinit var softly: SoftAssertions

    @LocalServerPort
    var port: Int = 0

    lateinit var webClient: WebTestClient

    @BeforeEach
    fun setup() {
        webClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun `get identity - success`() {
        // given
        val device = UUID.fromString("00000000-0000-0000-0002-000000000002")

        // when
        val result = webClient.get().uri("/identity/device-$device").exchange()

        // then
        result.expectStatus()
            .isOk
            .expectBody<Identity.Response>()
            .consumeWith {
                softly.assertThat(it.responseBody?.client).isEqualToUuid("00000000-0000-0000-0000-000000000000")
                softly.assertThat(it.responseBody?.node).isEqualToUuid("00000000-0000-0000-0003-000000000001")
                softly.assertThat(it.responseBody?.device).isEqualTo(device)
            }
    }

    @Test
    fun `get identity - fail device exist but not a node`() {
        // given
        val device = UUID.fromString("00000000-0000-0000-0002-000000000001")

        // when
        val result = webClient.get().uri("/identity/device-$device").exchange()

        // then
        result.expectStatus().isNotFound
    }

    @Test
    fun `get identity - fail device dont exist`() {
        // given
        val device = UUID.fromString("00000000-0000-0000-0002-000000000000")

        // when
        val result = webClient.get().uri("/identity/device-$device").exchange()

        // then
        result.expectStatus().isNotFound
    }
}
