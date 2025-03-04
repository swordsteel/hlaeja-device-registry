package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Node
import ltd.hlaeja.test.container.PostgresContainer
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

@PostgresContainer
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SoftAssertionsExtension::class)
class NodeEndpoint {

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
    fun `added node - success`() {
        // given
        val name = "Node 4"
        val device = UUID.fromString("00000000-0000-0000-0002-000000000001")
        val client = UUID.fromString("00000000-0000-0000-0000-000000000000")
        val request = Node.Request(device = device, client = client, name = name)

        // when
        val result = webClient.post().uri("/node").bodyValue(request).exchange()

        // then
        result.expectStatus()
            .isOk
            .expectBody<Node.Response>()
            .consumeWith {
                softly.assertThat(it.responseBody?.id?.version()).isEqualTo(7)
                softly.assertThat(it.responseBody?.device).isEqualTo(device)
                softly.assertThat(it.responseBody?.client).isEqualTo(client)
                softly.assertThat(it.responseBody?.name).isEqualTo(name)
            }
    }
}
