package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import sodam.backend.payment.domain.orders.model.PgStatus
import java.time.LocalDateTime

@Document(indexName = "order_history")
data class OrdersHistoryEntity(
    @Id
    var orderId: String,
    var userId: String? = null,
    var socialUserId: String? = null,
    var orderTotAmount: Long = 0,
    var discTotAmount: Long = 0,
    var paidTotAmount: Long = 0,
    var description: String = "",
    var pgStatus: PgStatus = PgStatus.CREATE,
    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second])
    var orderedAt: LocalDateTime,
    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second])
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second])
    var modifiedAt: LocalDateTime = LocalDateTime.now(),
)