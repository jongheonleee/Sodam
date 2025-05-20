package sodam.backend.payment.domain.orders.controller.request

data class SubscriptionsQuantityRequest(
    val subscriptionsId: String,
    val quantity: Long,
)