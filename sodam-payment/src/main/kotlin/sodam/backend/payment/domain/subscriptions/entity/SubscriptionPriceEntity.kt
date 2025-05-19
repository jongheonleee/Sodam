package sodam.backend.payment.domain.subscriptions.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.domain.model.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("subscription_price")
class SubscriptionPriceEntity(
    @Id
    var priceId: Long = 0,
    var subscriptionId: String? = null,
    var price: Long = 0, // 가격 부분 BigDecimal 사용 고려
    var discRate: Float = 0F,
    var discPrice: Long = 0,
    var salePrice: Long = 0,
    var validYN: Boolean = false,
    var startAt: LocalDateTime? = null,
    var endAt: LocalDateTime? = null,
): BaseEntity() {
}