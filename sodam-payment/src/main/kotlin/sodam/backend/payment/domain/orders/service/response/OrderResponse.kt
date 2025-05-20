package sodam.backend.payment.domain.orders.service.response

import sodam.backend.payment.domain.orders.model.PgStatus
import java.time.LocalDateTime

data class OrderResponse(
    val orderId: String,
    val userId: String? = null,
    val socialUserId: String? = null,
    val subscriptionId: String,
    val orderTotAmount: Long = 0,
    val discTotAmount: Long = 0,
    val paidTotAmount: Long = 0,
    val description: String, // 이거 필요함
    val amount: Long = 0,
    val pgOrderId: String,
    val pgKey: String? = null, // 이거 필요함
    val pgStatus: PgStatus, // 이거 필요함
    val pgRetryCount: Int = 0,
    val orderedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    val modifiedAt: LocalDateTime? = null,
    val subscriptions: List<SubscriptionQuantityResponse>,
)