package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Nodes
import ltd.hlaeja.test.container.PostgresTestContainer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@PostgresTestContainer
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SoftAssertionsExtension::class)
class NodesEndpoint {

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
    fun `get nodes default - success`() {
        // when
        val result = webClient.get().uri("/nodes").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Nodes.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(3)
            }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1,3",
            "2,0",
        ],
    )
    fun `get nodes by page - success`(page: Int, expected: Int) {
        // when
        val result = webClient.get().uri("/nodes/page-$page").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Nodes.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(expected)
            }
    }

    @Test
    fun `get nodes by pages - fail`() {
        // when
        val result = webClient.get().uri("/nodes/page-0").exchange()

        // then
        result.expectStatus().isBadRequest
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1,2,2",
            "2,2,1",
            "3,2,0",
        ],
    )
    fun `get nodes by page and show - success`(page: Int, show: Int, expected: Int) {
        // when
        val result = webClient.get().uri("/nodes/page-$page/show-$show").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Nodes.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(expected)
            }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "0,1",
            "1,0",
        ],
    )
    fun `get nodes by page and show - fail`(page: Int, show: Int) {
        // when
        val result = webClient.get().uri("/nodes/page-$page/show-$show").exchange()

        // then
        result.expectStatus().isBadRequest
    }
}
