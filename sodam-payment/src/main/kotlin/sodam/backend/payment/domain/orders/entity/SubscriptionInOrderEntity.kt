package sodam.backend.payment.domain.orders.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.golbal.common.model.BaseEntity


// R2DBC 복합키 지원 안함
@Table("orders_subscription")
class SubscriptionInOrderEntity(
    @Id
    var seq: Long = 0, // 더미 pk -> R2DBC 복합키 지원 안하기 때문에 우회해서 사용해야함, DDL에서 유니크 키로 설정
    var orderId: String? = null, // 실제 pk
    var subscriptionId: String? = null, // 실제 pk
    var orderPrice: Long? = 0,
    var orderAmount: Long? = 0,
): BaseEntity() {

}