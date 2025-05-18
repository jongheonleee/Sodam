package sodam.backend.payment.domain.payments.repository

import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.payments.entity.PaymentsEntity

interface PaymentCustomRepository {
    suspend fun insertOnly(entity: PaymentsEntity): PaymentsEntity
}