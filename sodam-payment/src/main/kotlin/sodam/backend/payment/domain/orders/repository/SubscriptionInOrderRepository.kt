package sodam.backend.payment.domain.orders.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.orders.entity.SubscriptionInOrderEntity

@Repository
interface SubscriptionInOrderRepository: CoroutineCrudRepository<SubscriptionInOrderEntity, Long> {
    suspend fun countByOrderId(orderId: String): Long
    suspend fun findAllByOrderId(orderId: String): List<SubscriptionInOrderEntity>
}