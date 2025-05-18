package sodam.backend.payment.domain.payments.repository

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.payments.entity.PaymentsEntity
import java.time.LocalDateTime

@Repository
class PaymentCustomRepositoryImpl(
    private val client: DatabaseClient,
): PaymentCustomRepository {

    override suspend fun insertOnly(entity: PaymentsEntity): PaymentsEntity {
        client.sql(
            """
            INSERT INTO payments (
                PAYMENT_ID,
                USER_ID,
                SOCIAL_USER_ID,
                ORDER_ID,
                PAYMENT_AMOUNT,
                PAYMENT_CODE,
                CARD_APPR_CODE,
                CARD_CANC_CODE,
                PAID_AT,
                PAID_STAT,
                CREATED_AT,
                CREATED_BY,
                MODIFIED_AT,
                MODIFIED_BY
            ) VALUES (
                :paymentId,
                :userId,
                :socialUserId,
                :orderId,
                :paymentAmount,
                :paymentCode,
                :cardApprCode,
                :cardCancCode,
                :paidAt,
                :paidStat,
                :createdAt,
                :createdBy,
                :modifiedAt,
                :modifiedBy
            )
            """
        )
            .bind("paymentId", entity.paymentId)
            .bind("userId", entity.userId)
            .bind("socialUserId", entity.socialUserId)
            .bind("orderId", entity.orderId)
            .bind("paymentAmount", entity.paymentAmount)
            .bind("paymentCode", entity.paymentCode)
            .bind("cardApprCode", entity.cardApprCode)
            .bind("cardCancCode", entity.cardCancCode)
            .bind("paidAt", entity.paidAt)
            .bind("paidStat", entity.paidStat)
            .bind("createdAt", entity.createdAt ?: LocalDateTime.now())
            .bind("createdBy", entity.createdBy ?: "system")
            .bind("modifiedAt", entity.modifiedAt ?: LocalDateTime.now())
            .bind("modifiedBy", entity.modifiedBy ?: "system")
            .then()
            .awaitSingleOrNull()

        return entity
    }
}