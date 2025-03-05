package ltd.hlaeja.controller

import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.test.container.PostgresContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@PostgresContainer
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TypesEndpoint {

    @LocalServerPort
    var port: Int = 0

    lateinit var webClient: WebTestClient

    @BeforeEach
    fun setup() {
        webClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun `get types default - success`() {
        // when
        val result = webClient.get().uri("/types").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Type.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(4)
            }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1,4",
            "2,0",
        ],
    )
    fun `get types by page - success`(page: Int, expected: Int) {
        // when
        val result = webClient.get().uri("/types/page-$page").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Type.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(expected)
            }
    }

    @Test
    fun `get types by pages - fail`() {
        // when
        val result = webClient.get().uri("/types/page-0").exchange()

        // then
        result.expectStatus().isBadRequest
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1,2,2",
            "2,2,2",
            "3,2,0",
        ],
    )
    fun `get types by page and show - success`(page: Int, show: Int, expected: Int) {
        // when
        val result = webClient.get().uri("/types/page-$page/show-$show").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Type.Response>>()
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
    fun `get types by page and show - fail`(page: Int, show: Int) {
        // when
        val result = webClient.get().uri("/types/page-$page/show-$show").exchange()

        // then
        result.expectStatus().isBadRequest
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "'',4",
            "v1,3",
            "v2,1",
            "v3,0",
        ],
    )
    fun `get types filter - success`(filter: String, expected: Int) {
        // when
        val result = webClient.get().uri("/types/filter-$filter").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Type.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(expected)
            }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "'',1,4",
            "'',2,0",
            "v1,1,3",
            "v1,2,0",
            "v2,1,1",
            "v2,2,0",
            "v3,1,0",
        ],
    )
    fun `get types by filter and page - success`(filter: String, page: Int, expected: Int) {
        // when
        val result = webClient.get().uri("/types/filter-$filter/page-$page").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Type.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(expected)
            }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "'',0",
            "v1,0",
        ],
    )
    fun `get types by filter and page - fail`(filter: String, page: Int) {
        // when
        val result = webClient.get().uri("/types/filter-$filter/page-$page").exchange()

        // then
        result.expectStatus().isBadRequest
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "'',1,2,2",
            "'',2,2,2",
            "'',3,2,0",
            "v1,1,2,2",
            "v1,2,2,1",
            "v1,3,2,0",
            "v2,1,2,1",
            "v2,2,2,0",
            "v3,1,2,0",
        ],
    )
    fun `get types by filter, page and show - success`(filter: String, page: Int, show: Int, expected: Int) {
        // when
        val result = webClient.get().uri("/types/filter-$filter/page-$page/show-$show").exchange()

        // then
        result.expectStatus().isOk()
            .expectBody<List<Type.Response>>()
            .consumeWith {
                assertThat(it.responseBody?.size).isEqualTo(expected)
            }
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "'',1,0",
            "'',0,1",
            "v1,1,0",
            "v1,0,1",
        ],
    )
    fun `get types by filter, page and show - fail`(filter: String, page: Int, show: Int) {
        // when
        val result = webClient.get().uri("/types/filter-$filter/page-$page/show-$show").exchange()

        // then
        result.expectStatus().isBadRequest
    }
}
