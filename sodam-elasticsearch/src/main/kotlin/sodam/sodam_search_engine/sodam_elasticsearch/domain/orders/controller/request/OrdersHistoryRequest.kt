package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.controller.request

import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity.OrdersHistoryEntity
import java.time.LocalDateTime

data class OrdersHistoryRequest(
    var orderId: String,
    var userId: String?,
    var socialUserId: String?,
    var orderTotAmount: Long?,
    var discTotAmount: Long?,
    var paidTotAmount: Long?,
    var description: String?,
    var pgStatus: PgStatus?,
    var orderedAt: LocalDateTime?,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var modifiedAt: LocalDateTime = LocalDateTime.now(),
) {

    fun toEntity(): OrdersHistoryEntity {
        return this.let { OrdersHistoryEntity(
            orderId = it.orderId,
            userId = it.userId,
            socialUserId = it.socialUserId,
            orderTotAmount = it.orderTotAmount ?: 0,
            discTotAmount = it.discTotAmount ?: 0,
            paidTotAmount = it.paidTotAmount ?: 0,
            description = it.description ?: "",
            pgStatus = it.pgStatus ?: PgStatus.CREATE,
            orderedAt = it.orderedAt ?: LocalDateTime.now(),
            createdAt = it.createdAt,
            modifiedAt = it.modifiedAt,
        ) }
    }

}