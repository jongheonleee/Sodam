package sodam.backend.payment.domain.subscriptions.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.model.BaseEntity
import java.time.LocalDateTime


@Table("subscriptions_history")
class SubscriptionHistoryEntity(
    @Id
    var historyId: Long = 0,
    var subscriptionId: String? = null,
    var startAt: LocalDateTime? = null,
    var endAt: LocalDateTime? = null,
    var subscriptionName: String? = null,
    var downCnt: Long = 0,
    var viewCnt: Long = 0,
    var subscriptionDesc: String? = null,
): BaseEntity() {
}