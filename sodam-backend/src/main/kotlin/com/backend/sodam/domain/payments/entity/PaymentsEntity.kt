package com.backend.sodam.domain.payments.entity

import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class PaymentsEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "PAYMENT_ID")
    val paymentId: UUID,

    // FK(추후에 연관관계 매핑)
    // - 주문 아이디 : 주문 히스토리 - 주문 = N : 1 ✅
    // - 구독권 아이디 : 주문 히스토리 - 구독권 = N : 1 [이거 FK 가지말고 하드코딩으로 밖아버리자] ✅
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user : UsersEntity,

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID")
    val subscriptionId: SubscriptionsEntity,

    // 가변 필드
    paymentAmount: Int,
    paymentCode: String,
    cardApprCode: String,
    cardCancCode: String,
    paidAt: LocalDateTime,
    paidStat: String
) : MutableBaseEntity() {

    @Column(name = "PAYMENT_AMOUNT")
    var paymentAmount = paymentAmount
        protected set

    @Column(name = "PAYMENT_CODE")
    var paymentCode = paymentCode
        protected set

    @Column(name = "CARD_APPR_CODE")
    var cardApprCode = cardApprCode
        protected set

    @Column(name = "CARD_CANC_CODE")
    var cardCancCode = cardCancCode
        protected set

    @Column(name = "PAID_AT")
    var paidAt = paidAt
        protected set

    @Column(name = "PAYD_STAT")
    var paidStat = paidStat
        protected set
}
