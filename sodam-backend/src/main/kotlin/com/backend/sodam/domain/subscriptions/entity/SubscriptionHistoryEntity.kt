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
@Table(name = "subscriptions_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SubscriptionHistoryEntity(
    @Id
    @Column(name = "HISTORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val historyId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "SUBSCRIPTION_ID")
    var subscriptionId: SubscriptionsEntity? = null,

    @Column(name = "SUBSCRIPTION_NAME")
    val subscriptionName: String,

    @Column(name = "SUBSCRIPTION_DESC", columnDefinition = "TEXT")
    val subscriptionDesc: String,

    @Column(name = "DOWN_CNT")
    val downCnt: Long,

    @Column(name = "VIEW_CNT")
    var viewCnt: Long,


    startAt: LocalDateTime,
    endAt: LocalDateTime,

): MutableBaseEntity() {
}