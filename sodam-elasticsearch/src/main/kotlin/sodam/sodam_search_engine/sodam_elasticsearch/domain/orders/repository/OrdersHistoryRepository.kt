package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity.OrdersHistoryEntity

@Repository
interface OrdersHistoryRepository: CoroutineCrudRepository<OrdersHistoryEntity, String> {
}