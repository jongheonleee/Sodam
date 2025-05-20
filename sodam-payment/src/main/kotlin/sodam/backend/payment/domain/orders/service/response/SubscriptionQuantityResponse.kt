package sodam.backend.payment.domain.orders.service.response

data class SubscriptionQuantityResponse(
    val subscriptionId: String,
    val subscriptionName: String,
    val price: Long,
    val quantity: Long,
)