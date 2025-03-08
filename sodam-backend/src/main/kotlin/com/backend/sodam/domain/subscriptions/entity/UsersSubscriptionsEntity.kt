package com.backend.sodam.domain.subscriptions.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "user_subscriptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersSubscriptionsEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "USER_SUBSCRIPTION_ID")
    val userSubscriptionId: UUID,

    // FK(추후에 연관관계 매핑)
    // - 회원 아이디 : 회원 보유 구독권 - 회원 = N : 1
    // - 구독권 아이디 : 회원 보유 구독권 - 구독권 = N : 1

    // 가변 필드
    startAt: LocalDateTime,
    endAt: LocalDateTime,
    validYN: Int
) : MutableBaseEntity() {

    @Column(name = "START_AT")
    var startAt = startAt
        protected set

    @Column(name = "END_AT")
    var endAt = endAt
        protected set

    @Column(name = "VALID_YN")
    var validYN = validYN
        protected set
}
