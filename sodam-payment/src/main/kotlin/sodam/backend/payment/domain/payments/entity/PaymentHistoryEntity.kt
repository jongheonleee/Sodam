package sodam.backend.payment.domain.payments.entity

import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.payments.model.PaymentStatus
import sodam.backend.payment.golbal.common.model.BaseEntity
import java.time.LocalDateTime
import java.util.*

@Table("payments_history")
class PaymentHistoryEntity(
    var paymentHistoryId: String = UUID.randomUUID().toString(),
    var paymentId: String? = null,
    var paymentStat: PaymentStatus = PaymentStatus.PENDING,
    var paymentAmount: Long? = null,
    var paymentCode: String? = null,
    var cardApprCode: String? = null,
    var cardCancCode: String? = null,
    var paidAt: LocalDateTime = LocalDateTime.now(),
    var cardCancCod: String? = null,
): BaseEntity() {
}