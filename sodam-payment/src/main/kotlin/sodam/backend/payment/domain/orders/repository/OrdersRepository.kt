package sodam.backend.payment.domain.orders.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.orders.entity.OrdersEntity

@Repository
interface OrdersRepository: CoroutineCrudRepository<OrdersEntity, String>, OrdersCustomRepository {
}