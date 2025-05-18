package sodam.backend.payment.domain.subscriptions.repository

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity
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
}