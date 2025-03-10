package com.backend.sodam.domain.orders.entity

import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class OrdersEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "ORDER_ID")
    val orderId: UUID,

    // FK(추후에 연관관계 매핑)
    // - 회원 아이디 : 주문 - 회원 = N : 1 ✅
    // - 구독권 아이디 : 주문 - 구독권 = 1 : 1 ✅
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user : UsersEntity,

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID")
    val subscriptionId: SubscriptionsEntity,

    // 가변 필드
    orderTotAmount: Int,
    discTotAmount: Int,
    paidTotAmount: Int,
    orderedAt: LocalDateTime
) : MutableBaseEntity() {

    @Column(name = "ORDER_TOT_AMOUNT")
    var orderTotAmount = orderTotAmount
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
