package sodam.backend.payment.domain.orders.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.orders.entity.OrdersEntity

@Repository
interface OrdersRepository: CoroutineCrudRepository<OrdersEntity, String>, OrdersCustomRepository {
    suspend fun findAllByUserIdOrderByCreatedAtDesc(userId: String): List<OrdersEntity>
    suspend fun findByPgOrderId(pgOrderId: String): OrdersEntity?
}