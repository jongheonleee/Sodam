package com.backend.sodam.domain.payments.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "payments_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class PaymentsHistoryEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "PAYMENT_HISTORY_ID")
    val paymentHistoryId: String,

    // FK(추후에 연관관계 처리)
    // - 결제 아이디 : 결제 이력 - 결제 = N : 1
    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID")
    val payment : PaymentsEntity,

    // 가변 필드
    paymentStat: String,
    paymentAmount: Int,
    paymentCode: String,
    cardApprCode: String,
    cardCancCode: String,
    paidAt: LocalDateTime

) : MutableBaseEntity() {
    @Column(name = "PAYMENT_STAT")
    var paymentStat = paymentStat
        protected set

    @Column(name = "PAYMENT_AMOUNT")
    var paymentAmount = paymentAmount
        protected set

    @Column(name = "PAYMENT_CODE")
    var paymentCode = paymentCode
        protected set

    @Column(name = "CARD_APPR_CODE")
    var cardApprCode = cardApprCode
        protected set

    @Column(name = "CARD_CANC_COD")
    var cardCancCode = cardCancCode
        protected set

    @Column(name = "PAID_AT")
    var paidAt = paidAt
        protected set
}
