package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.controller.request

import sodam.backend.payment.domain.orders.model.PgStatus

data class QuerySearch(
    val orderId: List<String>?,
    val userId: List<String>?,
    val socialUserId: List<String>?,
    val keyword: String?, // 이런 문자열 검색 -> ['이런' && '문자열' && '검색']
    val pgStatus: List<PgStatus>?,
    val fromDt: String?,
    val toDt: String?,
    val fromPaidAmount: Long?,
    val toPaidAmount: Long?,
    val pageSize: Int = 10,
    val pageNext: List<Long>? = null,
)