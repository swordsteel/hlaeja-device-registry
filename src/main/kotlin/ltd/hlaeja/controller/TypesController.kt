package ltd.hlaeja.controller

import jakarta.validation.constraints.Min
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ltd.hlaeja.library.deviceRegistry.Types
import ltd.hlaeja.service.TypeService
import ltd.hlaeja.util.Pagination.DEFAULT_PAGE
import ltd.hlaeja.util.Pagination.DEFAULT_SIZE
import ltd.hlaeja.util.toTypesResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TypesController(
    private val service: TypeService,
) {

    @GetMapping(
        "/types",
        "/types/page-{page}",
        "/types/page-{page}/show-{show}",
        "/types/filter-{filter}",
        "/types/filter-{filter}/page-{page}",
        "/types/filter-{filter}/page-{page}/show-{show}",
    )
    suspend fun getTypes(
        @PathVariable(required = false) @Min(1) page: Int = DEFAULT_PAGE,
        @PathVariable(required = false) @Min(1) show: Int = DEFAULT_SIZE,
        @PathVariable(required = false) filter: String? = null,
    ): Flow<Types.Response> = service.getTypes((page - 1) * show, show, filter)
        .map { it.toTypesResponse() }
}
