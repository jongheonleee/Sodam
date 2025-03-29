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
@Table(name = "subscription_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SubscriptionStatusEntity(

    @Id
    @Column(name = "STATUS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val statusId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID")
    var subscription: SubscriptionsEntity? = null,

    subscriptionStatus: Int, // 추후에 통합 코드 테이블에서 관리하게 만들 것, 그 과정에서 Enum 사용할 것
    startAt: LocalDateTime,
    endAt: LocalDateTime,
    validYN: Int
): MutableBaseEntity() {

    @Column(name = "SUBSCRIPTION_STATUS")
    var subscriptionStatus: Int = subscriptionStatus
        protected set

    @Column(name = "START_AT")
    var startAt: LocalDateTime = startAt
        protected set

    @Column(name = "END_AT")
    var endAt: LocalDateTime = endAt
        protected set

    @Column(name = "VALID_YN")
    var validYN: Int = validYN
        protected set
}