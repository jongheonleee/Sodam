package sodam.backend.payment.domain.orders.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.orders.model.OrderStatus
import sodam.backend.payment.golbal.common.model.BaseEntity
import java.time.LocalDateTime
import java.util.*

@Table("order_status")
class OrderStatusEntity(
    @Id
    var orderStatusId: String = UUID.randomUUID().toString(),
    // var userId: String? = null,
    var normalUserId: String? = null,
    var socialUserId: String? = null,
    var orderId: String? = null,
    var orderStatus: OrderStatus = OrderStatus.CREATED,
    var subscriptionId: String? = null,
    var ordTotAmount: Long = 0,
    var discTotAmount: Long = 0,
    var paidTotAmount: Long = 0,
    var orderedAt: LocalDateTime = LocalDateTime.now(),
): BaseEntity() {
}