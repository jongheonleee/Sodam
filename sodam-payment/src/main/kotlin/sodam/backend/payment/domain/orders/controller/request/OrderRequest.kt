package sodam.backend.payment.domain.orders.controller.request

data class OrderRequest(
    val userId: String,
    var subscriptions: List<SubscriptionsQuantityRequest>,
)
