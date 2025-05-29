package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository

import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.Criteria
import org.springframework.stereotype.Component
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity.OrdersHistoryEntity
import sodam.sodam_search_engine.sodam_elasticsearch.global.extension.toLocalDate
import kotlin.reflect.KProperty

@Component
class HistoryNativeRepository(
    private val template: ReactiveElasticsearchTemplate,
) {

    suspend fun search(request: QuerySearch) {
        Criteria().apply {
            request.orderId?.let { and(
                OrdersHistoryEntity::orderId.criteria.`in`(it)
            )}

            request.userId?.let {and(
                OrdersHistoryEntity::userId.criteria.`in`(it)
            )}

            request.keyword?.split(" ")?.toSet()?.forEach { and(
                OrdersHistoryEntity::description.criteria.contains(it)
            )}

            request.pgStatus?.let {and(
                OrdersHistoryEntity::pgStatus.criteria.`in`(it)
            )}

            request.fromDt?.toLocalDate()?.let {
                OrdersHistoryEntity::createdAt.criteria.greaterThanEqual(it)
            }
        }
    }
}

val KProperty<*>.criteria: Criteria
    get() = Criteria(this.name)


data class QuerySearch(
    val orderId: List<String>?,
    val userId: List<String>?,
    val keyword: String?, // 이런 문자열 검색 -> ['이런' && '문자열' && '검색']
    val pgStatus: List<PgStatus>?,
    val fromDt: String?,
    val toDt: String?,
    val fromPaidAmount: Long?,
    val toPaidAmount: Long?,
)
