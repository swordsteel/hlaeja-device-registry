package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.library.deviceRegistry.Devices
import ltd.hlaeja.test.container.PostgresTestContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@PostgresTestContainer
@SpringBootTest(webEnvironment = RANDOM_PORT)
class DevicesEndpoint {

    @LocalServerPort
    var port: Int = 0

    lateinit var webClient: WebTestClient

    @BeforeEach
    fun setup() {
        webClient = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Nested
    inner class GetDevices {

        @Test
        fun `get devices default - success`() {
            // when
            val result = webClient.get().uri("/devices").exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<List<Devices.Response>>()
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
        fun `get devices by page - success`(page: Int, expected: Int) {
            // when
            val result = webClient.get().uri("/devices/page-$page").exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<List<Devices.Response>>()
                .consumeWith {
                    assertThat(it.responseBody?.size).isEqualTo(expected)
                }
        }

        @Test
        fun `get devices by pages - fail`() {
            // when
            val result = webClient.get().uri("/devices/page-0").exchange()

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
        fun `get devices by page and show - success`(page: Int, show: Int, expected: Int) {
            // when
            val result = webClient.get().uri("/devices/page-$page/show-$show").exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<List<Devices.Response>>()
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
        fun `get devices by page and show - fail`(page: Int, show: Int) {
            // when
            val result = webClient.get().uri("/devices/page-$page/show-$show").exchange()

            // then
            result.expectStatus().isBadRequest
        }
    }

    @Nested
    inner class GetDevicesByType {

        @Test
        fun `get devices for type default - success`() {
            // when
            val result = webClient.get()
                .uri("/devices/type-00000000-0000-0000-0001-000000000001")
                .exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<List<Devices.Response>>()
                .consumeWith {
                    assertThat(it.responseBody?.size).isEqualTo(2)
                }
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "00000000-0000-0000-0001-000000000001,1,2",
                "00000000-0000-0000-0001-000000000001,2,0",
            ],
        )
        fun `get devices for type by page - success`(type: UUID, page: Int, expected: Int) {
            // when
            val result = webClient.get()
                .uri("/devices/type-$type/page-$page")
                .exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<List<Devices.Response>>()
                .consumeWith {
                    assertThat(it.responseBody?.size).isEqualTo(expected)
                }
        }

        @Test
        fun `get devices for type by pages - fail`() {
            // when
            val result = webClient.get()
                .uri("/devices/type-00000000-0000-0000-0001-000000000001/page-0")
                .exchange()

            // then
            result.expectStatus().isBadRequest
        }

        @ParameterizedTest
        @CsvSource(
            value = [
                "00000000-0000-0000-0001-000000000001,1,1,1",
                "00000000-0000-0000-0001-000000000001,2,1,1",
                "00000000-0000-0000-0001-000000000001,3,1,0",
            ],
        )
        fun `get devices for type by page and show - success`(type: UUID, page: Int, show: Int, expected: Int) {
            // when
            val result = webClient.get()
                .uri("/devices/type-$type/page-$page/show-$show")
                .exchange()

            // then
            result.expectStatus().isOk()
                .expectBody<List<Devices.Response>>()
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
        fun `get devices for type by page and show - fail`(page: Int, show: Int) {
            // when
            val result = webClient.get()
                .uri("/devices/type-00000000-0000-0000-0001-000000000001/page-$page/show-$show")
                .exchange()

            // then
            result.expectStatus().isBadRequest
        }
    }
}
