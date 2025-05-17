package sodam.backend.payment.domain.orders.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.backend.payment.golbal.common.model.BaseEntity
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity

@Table("orders")
class OrdersEntity(
    // 내가 정의한 테이블 필드
    @Id
    var orderId: String? = null,
    var userId: String? = null,
    var socialUserId: String? = null,
    var subscriptionId: String? = null,
    var orderTotAmount: Long = 0,
    var distTotAmount: Long = 0,
    var paidTotAmount: Long = 0,
    var description: String? = null, // 이거 필요함
    var amount: Long = 0,
    var pgOrderId: String? = null,
    var pgKey: String? = null, // 이거 필요함
    var pgStatus: PgStatus = PgStatus.CREATE, // 이거 필요함
    var pgRetryCount: Int = 0,
): BaseEntity() {

}