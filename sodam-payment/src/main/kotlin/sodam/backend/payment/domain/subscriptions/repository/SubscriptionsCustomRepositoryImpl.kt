package sodam.backend.payment.domain.subscriptions.repository

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity
import sodam.backend.payment.domain.subscriptions.service.response.SubscriptionSaleInfo
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
class SubscriptionsCustomRepositoryImpl(
    private val client: DatabaseClient,
): SubscriptionsCustomRepository {

    override suspend fun insertOnly(entity: SubscriptionsEntity): SubscriptionsEntity {
        client.sql(
            """
                INSERT INTO subscriptions (
                    SUBSCRIPTION_ID,
                    SUBSCRIPTION_NAME,
                    subscription_content,
                    VIEW_CNT,
                    DOWN_CNT,
                    created_at,
                    created_by,
                    modified_at,
                    modified_by
                ) VALUES (
                    :subscriptionId, 
                    :subscriptionName, 
                    :subscriptionContent, 
                    :viewCnt, 
                    :downCnt, 
                    :createdAt, 
                    :createdBy,
                    :modifiedAt, 
                    :modifiedBy
                )
            """
        )
            .bind("subscriptionId", entity.subscriptionId!!)
            .bind("subscriptionName", entity.subscriptionName!!)
            .bind("subscriptionContent", entity.subscriptionContent!!)
            .bind("viewCnt", entity.viewCnt)
            .bind("downCnt", entity.downCnt)
            .bind("createdAt", entity.createdAt ?: LocalDateTime.now())
            .bind("createdBy", entity.createdBy)
            .bind("modifiedAt", entity.modifiedAt ?: LocalDateTime.now())
            .bind("modifiedBy", entity.modifiedBy)
            .then()
            .awaitFirstOrNull()
        return entity
    }

    override suspend fun selectSubscriptionSaleInfo(subscriptionId: String): SubscriptionSaleInfo {
        val sql = """
            SELECT
                s.SUBSCRIPTION_ID AS SUBSCRIPTION_ID,
                s.SUBSCRIPTION_NAME AS SUBSCRIPTION_NAME,
                s.subscription_content AS SUBSCRIPTION_CONTENT,
                s.VIEW_CNT AS VIEW_CNT,
                s.DOWN_CNT AS DOWN_CNT,
                sp.price AS PRICE,
                ROUND(sp.price * sp.DISC_RATE) AS DISC_PRICE,
                sp.DISC_RATE AS DISC_RATE,
                ROUND(sp.price - (sp.price * sp.DISC_RATE)) AS SALE_PRICE,
                st.valid_yn AS IS_SALE
            FROM subscriptions s
            LEFT JOIN subscription_price sp ON s.SUBSCRIPTION_ID = sp.SUBSCRIPTION_ID
            LEFT JOIN subscription_status st ON s.SUBSCRIPTION_ID = st.SUBSCRIPTION_ID
            WHERE s.SUBSCRIPTION_ID = :subscriptionId
            AND st.valid_yn = 1 
            AND sp.START_AT <= NOW() AND NOW() <= sp.END_AT
            AND sp.valid_yn = 1 
            AND st.START_AT <= NOW() AND NOW() <= st.END_AT
        """.trimIndent()

        return client.sql(sql)
            .bind("subscriptionId", subscriptionId)
            .map { row: Row, _: RowMetadata ->
                SubscriptionSaleInfo(
                    subscriptionId = row.get("SUBSCRIPTION_ID", String::class.java) ?: "",
                    subscriptionName = row.get("SUBSCRIPTION_NAME", String::class.java) ?: "",
                    subscriptionContent = row.get("SUBSCRIPTION_CONTENT", String::class.java) ?: "",
                    viewCnt = row.get("VIEW_CNT", Integer::class.java)?.toInt() ?: 0,
                    downCnt = row.get("DOWN_CNT", Integer::class.java)?.toInt() ?: 0,
                    price = row.get("PRICE", BigDecimal::class.java)?.longValueExact() ?: 0L,
                    discPrice = row.get("DISC_PRICE", BigDecimal::class.java)?.longValueExact() ?: 0L,
                    discRate = row.get("DISC_RATE", BigDecimal::class.java)?.toFloat() ?: 0f,
                    salePrice = row.get("SALE_PRICE", BigDecimal::class.java)?.longValueExact() ?: 0L,
                    isSale = (row.get("IS_SALE", Integer::class.java) ?: 0) == 1)
            }
            .one()
            .awaitSingle()
    }
}