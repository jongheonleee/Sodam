package sodam.backend.payment.domain.orders.repository

import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity

interface OrdersCustomRepository {
    suspend fun insertOnly(entity: OrdersEntity): OrdersEntity
}