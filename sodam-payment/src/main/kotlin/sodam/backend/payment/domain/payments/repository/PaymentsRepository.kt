package sodam.backend.payment.domain.payments.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.payments.entity.PaymentsEntity

@Repository
interface PaymentsRepository: CoroutineCrudRepository<PaymentsEntity, String>, PaymentCustomRepository {
}