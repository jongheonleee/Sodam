package sodam.backend.payment.domain.orders.repository

import kotlinx.coroutines.flow.toList
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import sodam.backend.payment.domain.common.query
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.backend.payment.domain.orders.service.command.OrderQueryHistory
import sodam.backend.payment.golbal.extension.toLocalDate
import java.time.LocalDateTime

@Repository
class OrderHistoryCustomRepositoryImpl(
    private val dbClient: DatabaseClient,
): OrderHistoryCustomRepository {

    // 이 부분도 추후에 캐시 활용해서 성능 개선하느게 좋을 듯
    override suspend fun getHistories(request: OrderQueryHistory): List<OrdersEntity> {
        val param = HashMap<String, Any>().apply {
            put("userId", request.userId)
            put("pgStatus", listOf(PgStatus.CAPTURE_REQUEST, PgStatus.CAPTURE_RETRY, PgStatus.CAPTURE_SUCCESS)
                            .map { it.name })
            put("limit", request.limit)
            put("offset", (request.page - 1) * request.limit)
        }

        var sql = dbClient.sql("""
                SELECT  ORDER_ID,
                        USER_ID,
                        SOCIAL_USER_ID,
                        DESCRIPTION,
                        PAID_TOT_AMOUNT,
                        ORDER_TOT_AMOUNT,
                        DISC_TOT_AMOUNT,
                        PG_ORDER_ID,
                        PG_KEY,
                        PG_STATUS,
                        PG_RETRY_COUNT,
                        CREATED_AT,
                        MODIFIED_AT
                FROM    orders
                WHERE   USER_ID = :userId
                AND     PG_STATUS IN (:pgStatus)
                ${request.keyword.query { 
                    val keywords = it.trim().split(" ")
                    if (keywords.isEmpty()) {
                        ""
                    }
                    else {
                        val lines = ArrayList<String>()
                        repeat(keywords.size) { i ->
                            val key = "keyword_$i"
                            param[key] = keywords[i].let { "%$it%" }
                            lines.add("DESCRIPTION LIKE :keyword_$i")
                        }
                        
                        "AND ( ${lines.joinToString(" AND ")} )"
                    }
            }}
            ${request.fromDate.query { 
                param["fromDate"] = it.toLocalDate()
                "AND    :fromDate <= CREATED_AT"
            }}
            ${request.toDate.query { 
                param["toDate"] = it.toLocalDate().plusDays(1)
                "AND    CREATED_AT < :toDate"
            }}
            ${request.fromAmount.query { 
                param["fromAmount"] = it
                "AND    :fromAmount <= PAID_TOT_AMOUNT"
            }}
            ${request.toAmount.query { 
                param["toAmount"] = it 
                "AND    PAID_TOT_AMOUNT <= :toAmount"
            }}
            ORDER BY created_at DESC
            LIMIT :limit
            OFFSET :offset
        """.trimIndent())

        param.forEach { key, value -> sql = sql.bind(key, value) }

        return sql.map { row -> OrdersEntity(
            orderId = row.get("ORDER_ID") as String,
            userId = row.get("USER_ID") as? String,
            socialUserId = row.get("SOCIAL_USER_ID") as? String,
            description = row.get("DESCRIPTION") as String,
            paidTotAmount = row.get("PAID_TOT_AMOUNT") as Long,
            orderTotAmount = row.get("ORDER_TOT_AMOUNT") as Long,
            discTotAmount = row.get("DISC_TOT_AMOUNT") as Long,
            pgStatus = (row.get("PG_STATUS") as String).let { PgStatus.valueOf(it) },
        ).apply {
            createdAt = row.get("CREATED_AT") as? LocalDateTime
            modifiedAt = row.get("MODIFIED_AT") as? LocalDateTime
        } } .flow()
            .toList()
    }
}
