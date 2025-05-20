package sodam.backend.payment.domain.payments.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.payments.model.PaymentStatus
import sodam.backend.payment.domain.common.model.BaseEntity
import java.time.LocalDateTime
import java.util.*

@Table("payments")
class PaymentsEntity(
    @Id
    var paymentId: String? = null,
    var userId: String? = null,
    var socialUserId: String? = null,
    var orderId: String? = null,
    var paymentAmount: Long? = null,
    var paymentCode: String? = null,
    var cardApprCode: String? = null,
    var cardCancCode: String? = null,
    var paidAt: LocalDateTime = LocalDateTime.now(),
    var paidStat: PaymentStatus = PaymentStatus.CREATED,
    var subscriptionId: String? = null,
    var paydStat: String? = null,
): BaseEntity() {
}