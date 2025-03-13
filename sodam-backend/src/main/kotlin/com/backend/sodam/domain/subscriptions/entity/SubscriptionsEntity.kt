package com.backend.sodam.domain.subscriptions.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "subscriptions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SubscriptionsEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "SUBSCRIPTION_ID")
    val subscriptionId: String,

    // 가변 필드
    subscriptionName: String,
    subscriptionContent: String,
    viewCnt: Int,
    downCnt: Int
) : MutableBaseEntity() {

    @Column(name = "SUBSCRIPTION_NAME")
    var subscriptionName = subscriptionName
        protected set

    @Column(name = "SUBSCRIPTION_CONTENT")
    var subscriptionContent = subscriptionContent
        protected set

    @Column(name = "VIEW_CNT")
    var viewCnt = viewCnt
        protected set

    @Column(name = "DOWN_CNT")
    var downCnt = downCnt
        protected set
}
