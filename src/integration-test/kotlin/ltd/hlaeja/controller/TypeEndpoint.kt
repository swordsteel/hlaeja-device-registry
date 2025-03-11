package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.test.container.PostgresContainer
import ltd.hlaeja.test.isEqualToUuid
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.MediaType.APPLICATION_JSON
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
            val name = "Thing 5 v1"
            val description = "Thing 5 description"
            val request = Type.Request(
                name = name,
                description = description,
            )

            // when
            val result = webClient.post().uri("/type").bodyValue(request).exchange()

            // then
            result.expectStatus()
                .isCreated
                .expectBody<Type.Response>()
                .consumeWith {
                    softly.assertThat(it.responseBody?.id?.version()).isEqualTo(7)
                    softly.assertThat(it.responseBody?.name).isEqualTo(name)
                    softly.assertThat(it.responseBody?.description).isEqualTo(description)
                }
        }

        @Test
        fun `added type - fail name take`() {
            // given
            val request = Type.Request(
                name = "Thing 1 v1",
                description = "Thing 1 description",
            )

            // when
            val result = webClient.post().uri("/type").bodyValue(request).exchange()

            // then
            result.expectStatus().isEqualTo(CONFLICT)
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "{}",
                "{'name': 'Thing 0 v1'}",
                "{'description': 'Thing 0 description'}",
            ],
        )
        fun `added type - fail bad request`(jsonRequest: String) {
            // when
            val result = webClient.post()
                .uri("/type")
                .contentType(APPLICATION_JSON) // Set Content-Type header
                .bodyValue(jsonRequest) // Send raw JSON string
                .exchange()

            // then
            result.expectStatus().isBadRequest
        }
    }

    @Nested
    inner class GetType {

        @Test
        fun `added type - success`() {
            // when
            val result = webClient.get().uri("/type-00000000-0000-0000-0001-000000000001").exchange()

            // then
            result.expectStatus()
                .isOk
                .expectBody<Type.Response>()
                .consumeWith {
                    softly.assertThat(it.responseBody?.id).isEqualToUuid("00000000-0000-0000-0001-000000000001")
                    softly.assertThat(it.responseBody?.name).isEqualTo("Thing 1 v1")
                    softly.assertThat(it.responseBody?.description).isEqualTo("Thing 1 description")
                }
        }

        @Test
        fun `get type - fail not found`() {
            // when
            val result = webClient.get().uri("/type-00000000-0000-0000-0000-000000000000").exchange()

            // then
            result.expectStatus().isNotFound
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "zzzzzzzz-zzzz-zzzz-zzzz-zzzzzzzzzzzz",
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz",
                "00000000000000000000000000000000",
                "0",
            ],
        )
        fun `get type - fail bad request`(uuid: String) {
            // when
            val result = webClient.get().uri("/type-$uuid").exchange()

            // then
            result.expectStatus().isBadRequest
        }
    }

    @Nested
    inner class UpdateType {

        @ParameterizedTest
        @CsvSource(
            value = [
                "Thing 4 v1,Thing 4 description update",
                "Thing 4 v1 update,Thing 4 description update",
                "Thing 4 v1,Thing 4 description",
            ],
        )
        fun `update type - success`(name: String, description: String) {
            // given
            val request = Type.Request(
                name = name,
                description = description,
            )

            // when
            val result = webClient.put()
                .uri("/type-00000000-0000-0000-0001-000000000004")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()

            // then
            result.expectStatus()
                .isOk
                .expectBody<Type.Response>()
                .consumeWith {
                    softly.assertThat(it.responseBody?.id).isEqualToUuid("00000000-0000-0000-0001-000000000004")
                    softly.assertThat(it.responseBody?.name).isEqualTo(name)
                    softly.assertThat(it.responseBody?.description).isEqualTo(description)
                }
        }

        @Test
        fun `update type - success no change`() {
            // given
            val request = Type.Request(
                name = "Thing 1 v1",
                description = "Thing 1 description",
            )

            // when
            val result = webClient.put()
                .uri("/type-00000000-0000-0000-0001-000000000001")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()

            // then
            result.expectStatus().isEqualTo(ACCEPTED)
        }

        @Test
        fun `update type - fail invalid id`() {
            // given
            val request = Type.Request(
                name = "Thing 0 v1",
                description = "Thing 0 description",
            )

            // when
            val result = webClient.put()
                .uri("/type-00000000-0000-0000-0001-000000000000")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()

            // then
            result.expectStatus().isNotFound
        }

        @Test
        fun `update type - fail name take`() {
            // given
            val request = Type.Request(
                name = "Thing 2 v1",
                description = "Thing 2 description",
            )

            // when
            val result = webClient.put()
                .uri("/type-00000000-0000-0000-0001-000000000001")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()

            // then
            result.expectStatus().isEqualTo(CONFLICT)
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "{}",
                "{'name': 'Thing 0 v1'}",
                "{'description': 'Thing 0 description'}",
            ],
        )
        fun `update type - fail bad data request`(jsonRequest: String) {
            // when
            val result = webClient.put()
                .uri("/type-00000000-0000-0000-0001-000000000001")
                .contentType(APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()

            // then
            result.expectStatus().isBadRequest
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "zzzzzzzz-zzzz-zzzz-zzzz-zzzzzzzzzzzz",
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz",
                "00000000000000000000000000000000",
                "0",
            ],
        )
        fun `update type - fail bad id request`(uuid: String) {
            // given
            val request = Type.Request(
                name = "Thing 0 v1",
                description = "Thing 0 description",
            )

            // when
            val result = webClient.put()
                .uri("/type-$uuid")
                .contentType(APPLICATION_JSON)
                .bodyValue(request)
                .exchange()

            // then
            result.expectStatus().isBadRequest
        }
    }
}
