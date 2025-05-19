package sodam.backend.payment.domain.orders.repository

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import java.time.LocalDateTime

@Repository
class OrdersCustomRepositoryImpl(
    private val client: DatabaseClient,
): OrdersCustomRepository {

    override suspend fun insertOnly(entity: OrdersEntity): OrdersEntity {
        client.sql(
            """
            INSERT INTO orders (
                ORDER_ID,
                USER_ID,
                SOCIAL_USER_ID,
                SUBSCRIPTION_ID,
                ORDER_TOT_AMOUNT,
                DISC_TOT_AMOUNT,
                PAID_TOT_AMOUNT,
                DESCRIPTION,
                AMOUNT,
                PG_ORDER_ID,
                PG_KEY,
                PG_STATUS,
                PG_RETRY_COUNT,
                ORDERED_AT,
                CREATED_AT,
                CREATED_BY,
                MODIFIED_AT,
                MODIFIED_BY
            )
            VALUES (
                :orderId,
                :userId,
                :socialUserId,
                :subscriptionId,
                :orderTotAmount,
                :discTotAmount,
                :paidTotAmount,
                :description,
                :amount,
                :pgOrderId,
                :pgKey,
                :pgStatus,
                :pgRetryCount,
                :orderedAt,
                :createdAt,
                :createdBy,
                :modifiedAt,
                :modifiedBy
            )
            """
        )
            .bind("orderId", entity.orderId)
            .bind("userId", entity.userId)
            .bind("socialUserId", entity.socialUserId)
            .bind("subscriptionId", entity.subscriptionId)
            .bind("orderTotAmount", entity.orderTotAmount)
            .bind("discTotAmount", entity.discTotAmount)
            .bind("paidTotAmount", entity.paidTotAmount)
            .bind("description", entity.description ?: "-")
            .bind("amount", entity.amount)
            .bind("pgOrderId", entity.pgOrderId ?: "-")
            .bind("pgKey", entity.pgKey ?: "system")
            .bind("pgStatus", entity.pgStatus.name ?: "-")
            .bind("pgRetryCount", entity.pgRetryCount ?: "-")
            .bind("orderedAt", entity.orderedAt ?: LocalDateTime.now())
            .bind("createdAt", entity.createdAt ?: LocalDateTime.now())
            .bind("createdBy", entity.createdBy ?: "system")
            .bind("modifiedAt", entity.modifiedAt ?: LocalDateTime.now())
            .bind("modifiedBy", entity.modifiedBy ?: "system")
            .then()
            .awaitSingleOrNull()

        return entity
    }
}