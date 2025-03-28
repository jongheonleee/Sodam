package com.backend.sodam.domain.subscriptions.entity

import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "user_subscriptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersSubscriptionsEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "USER_SUBSCRIPTION_ID")
    val userSubscriptionId: String,

    // FK(추후에 연관관계 매핑)
    // - 회원 아이디 : 회원 보유 구독권 - 회원 = N : 1
    // - 구독권 아이디 : 회원 보유 구독권 - 구독권 = N : 1
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    var user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    var socialUser: SocialUsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID")
    val subscription: SubscriptionsEntity,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "SUBSCRIPTION_NAME")
    val subscriptionName: SubscriptionsType,

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

    fun toDomain(): UserSubscription {
        return UserSubscription(
            userId = if (this.user != null) this.user!!.userId else this.socialUser!!.socialUserId,
            subscriptionType = this.subscriptionName,
            startAt = this.startAt,
            endAt = this.endAt,
            validYn = this.validYN == 0
        )
    }
}
