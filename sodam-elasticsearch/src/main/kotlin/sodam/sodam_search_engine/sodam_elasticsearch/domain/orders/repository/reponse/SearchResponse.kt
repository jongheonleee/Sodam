package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository.reponse

import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity.OrdersHistoryEntity

data class SearchResponse(
    val items: List<OrdersHistoryEntity>,
    val total: Long,
    val pageNext: List<Any>?,
)