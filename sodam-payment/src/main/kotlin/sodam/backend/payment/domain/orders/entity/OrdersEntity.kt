package sodam.backend.payment.domain.orders.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.backend.payment.domain.model.BaseEntity
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity
import java.time.LocalDateTime

@Table("orders")
class OrdersEntity(
    // 내가 정의한 테이블 필드
    @Id
    var orderId: String? = null,
    var userId: String? = null,
    var socialUserId: String? = null,
    var subscriptionId: String? = null, // 이 부분 제거해야함
    var orderTotAmount: Long = 0,
    var discTotAmount: Long = 0,
    var paidTotAmount: Long = 0,
    var description: String? = null, // 이거 필요함
    var amount: Long = 0,
    var pgOrderId: String? = null,
    var pgKey: String? = null, // 이거 필요함
    var pgStatus: PgStatus = PgStatus.CREATE, // 이거 필요함
    var pgRetryCount: Int = 0,
    var orderedAt: LocalDateTime? = LocalDateTime.now(),
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrdersEntity

        return orderId == other.orderId
    }

    override fun hashCode(): Int {
        return orderId?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "OrdersEntity(orderId=$orderId, userId=$userId, socialUserId=$socialUserId, subscriptionId=$subscriptionId, orderTotAmount=$orderTotAmount, discTotAmount=$discTotAmount, paidTotAmount=$paidTotAmount, description=$description, amount=$amount, pgOrderId=$pgOrderId, pgKey=$pgKey, pgStatus=$pgStatus, pgRetryCount=$pgRetryCount, orderedAt=$orderedAt)"
    }


}