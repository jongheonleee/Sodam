package sodam.backend.payment.domain.orders.repository

import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.service.command.OrderQueryHistory

interface OrderHistoryCustomRepository {
    suspend fun getHistories(request: OrderQueryHistory): List<OrdersEntity>
}