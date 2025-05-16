package com.backend.sodam.domain.orders.entity

import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "orders_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class OrdersHistoryEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "ORDER_HISTORY_ID")
    val orderHistoryId: String,

    // FK(추후에 연관관계 매핑)
    // - 주문 아이디 : 주문 히스토리 - 주문 : N : 1 ✅
    // - 구독권 아이디 : 주문 히스토리 - 구독권 : N : 1 [이거 FK 가지말고 하드코딩으로 밖아버리자] ✅
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    val order: OrdersEntity,

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID")
    val subscriptionId: SubscriptionsEntity,

    // 가변 필드
    finOrderStatus: String,
    ordTotAmount: Int,
    discTotAmount: Int,
    paidTotAmount: Int,
    orderedAt: LocalDateTime
) : MutableBaseEntity() {

    @Column(name = "FIN_ORDER_STATUS")
    var finOrderStatus = finOrderStatus
        protected set

    @Column(name = "ORD_TOT_AMOUNT")
    var ordTotAmount = ordTotAmount
        protected set

    @Column(name = "DISC_TOT_AMOUNT")
    var discTotAmount = discTotAmount
        protected set

    @Column(name = "PAID_TOT_AMOUNT")
    var paidTotAmount = paidTotAmount
        protected set

    @Column(name = "ORDERED_AT")
    var orderedAt = orderedAt
        protected set
}
