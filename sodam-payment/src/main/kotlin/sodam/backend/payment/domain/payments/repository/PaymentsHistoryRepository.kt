package sodam.backend.payment.domain.payments.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.payments.entity.PaymentHistoryEntity
import sodam.backend.payment.domain.payments.entity.PaymentsEntity

@Repository
interface PaymentsHistoryRepository: CoroutineCrudRepository<PaymentHistoryEntity, String> {
}