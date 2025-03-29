package com.backend.sodam.domain.subscriptions.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "subscription_price")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SubscriptionPriceEntity(
    @Id
    @Column(name = "PRICE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val priceId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID")
    var subscription: SubscriptionsEntity? = null,

    @Column(name = "PRICE")
    val price: Long,

    @Column(name = "DISC_PRICE")
    val discPrice: Double,

    @Column(name = "SALE_PRICE")
    val salePrice: Long,

    validYN: Int,
    startAt: LocalDateTime,
    endAt: LocalDateTime,
): MutableBaseEntity() {

    @Column(name = "VALID_YN")
    var validYN: Int = validYN
        protected set

    @Column(name = "START_AT")
    var startAt: LocalDateTime = startAt
        protected set

    @Column(name = "END_AT")
    var endAt: LocalDateTime = endAt
        protected set

}