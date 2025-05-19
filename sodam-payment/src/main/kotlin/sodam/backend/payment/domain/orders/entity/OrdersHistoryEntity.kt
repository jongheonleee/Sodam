package sodam.backend.payment.domain.orders.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.backend.payment.domain.model.BaseEntity
import java.time.LocalDateTime

@Table("orders_history")
class OrdersHistoryEntity(
    @Id
    var orderHistoryId: String = "",
    var orderId: String? = null,
    var subscriptionId: String? = null,
    var finOrderStatus: PgStatus = PgStatus.CREATE,
    var ordTotAmount: Long = 0,
    var discTotAmount: Long = 0,
    var paidTotAmount: Long = 0,
    var orderedAt: LocalDateTime = LocalDateTime.now(),
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrdersHistoryEntity

        return orderHistoryId == other.orderHistoryId
    }

    override fun hashCode(): Int {
        return orderHistoryId.hashCode()
    }

    override fun toString(): String {
        return "OrdersHistoryEntity(orderHistoryId='$orderHistoryId', orderId=$orderId, subscriptionId=$subscriptionId, finOrderStatus=$finOrderStatus, ordTotAmount=$ordTotAmount, discTotAmount=$discTotAmount, paidTotAmount=$paidTotAmount, orderedAt=$orderedAt)"
    }


}