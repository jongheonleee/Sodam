package sodam.backend.payment.domain.subscriptions.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.subscriptions.model.SubscriptionStatus
import sodam.backend.payment.domain.common.model.BaseEntity
import java.time.LocalDateTime

@Table("subscription_status")
class SubscriptionStatusEntity(
    @Id
    var statusId: Long? = null,
    var subscriptionId: String? = null,
    var subscriptionStatus: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    var startAt: LocalDateTime? = null,
    var endAt: LocalDateTime? = null,
    var validYN: Boolean = true,
): BaseEntity() {
}