package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.*
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.data.elasticsearch.core.query.CriteriaQuery
import org.springframework.stereotype.Component
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.controller.request.QuerySearch
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity.OrdersHistoryEntity
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository.reponse.SearchResponse
import sodam.sodam_search_engine.sodam_elasticsearch.global.extension.toLocalDate
import kotlin.reflect.KProperty

@Component
class OrdersHistoryNativeRepository(
    private val template: ReactiveElasticsearchTemplate,
) {

    suspend fun search(request: QuerySearch): SearchResponse {
        val criteria = Criteria().apply {
            request.orderId?.let {and(
                OrdersHistoryEntity::orderId.criteria.`in`(it)
            )}

            request.userId?.let {and(
                OrdersHistoryEntity::userId.criteria.`in`(it)
            )}

            request.socialUserId?.let {and(
                OrdersHistoryEntity::socialUserId.criteria.`in`(it)
            )}

            request.keyword?.split(" ")?.toSet()?.forEach {and(
                OrdersHistoryEntity::description.criteria.contains(it)
            )}

            request.pgStatus?.let {and(
                OrdersHistoryEntity::pgStatus.criteria.`in`(it)
            )}

            request.fromDt?.toLocalDate()?.atStartOfDay()?.let {and(
                OrdersHistoryEntity::createdAt.criteria.greaterThanEqual(it)
            )}

            request.toDt?.toLocalDate()?.plusDays(1)?.atStartOfDay()?.let {and(
                OrdersHistoryEntity::createdAt.criteria.lessThan(it)
            )}

            request.fromPaidAmount?.let {and(
                OrdersHistoryEntity::paidTotAmount.criteria.greaterThanEqual(it)
            )}

            request.toPaidAmount?.let {and(
                OrdersHistoryEntity::paidTotAmount.criteria.lessThan(it)
            )}
        }

        val query = CriteriaQuery(criteria, PageRequest.of(0, request.pageSize)).apply {
            sort = OrdersHistoryEntity::createdAt.sort(DESC)
            searchAfter = request.pageNext
        }

        return template.searchForPage(query, OrdersHistoryEntity::class.java).awaitSingle().let { response ->
            SearchResponse(
                response.content.map { it.content },
                response.totalElements,
                response.content.lastOrNull()?.sortValues,
            )
        }
    }
}

val KProperty<*>.criteria: Criteria
    get() = Criteria(this.name)

fun KProperty<*>.sort(direction: Direction = ASC): Sort {
    return Sort.by(direction, this.name)
}


