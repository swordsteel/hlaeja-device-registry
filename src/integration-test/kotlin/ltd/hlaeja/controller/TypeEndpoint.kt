package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.test.container.PostgresContainer
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
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@PostgresContainer
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SoftAssertionsExtension::class)
class TypeEndpoint {

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
    inner class CreateType {

        @Test
        fun `added type - success`() {
            // given
            val name = "Thing 5"
            val request = Type.Request(
                name = name,
            )

            // when
            val result = webClient.post().uri("/type").bodyValue(request).exchange()

            // then
            result.expectStatus()
                .isOk
                .expectBody<Type.Response>()
                .consumeWith {
                    softly.assertThat(it.responseBody?.id?.version()).isEqualTo(7)
                    softly.assertThat(it.responseBody?.name).isEqualTo(name)
                }
        }

        @Test
        fun `added type - fail name take`() {
            // given
            val request = Type.Request(
                name = "Thing 1 v1",
            )

            // when
            val result = webClient.post().uri("/type").bodyValue(request).exchange()

            // then
            result.expectStatus().isEqualTo(CONFLICT)
        }
    }
}
