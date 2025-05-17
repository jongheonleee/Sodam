package sodam.backend.payment.domain.subscriptions.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import sodam.backend.payment.golbal.common.model.BaseEntity

@Table("subscriptions")
class SubscriptionsEntity(
    // 필드 내용 다시 정리해야함
    @Id
    var subscriptionId: String? = null,
    var subscriptionName: String? = null,
    var subscriptionContent: String? = null,
    var viewCnt: Long = 0,
    var downCnt: Long = 0,
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubscriptionsEntity

        return subscriptionId == other.subscriptionId
    }

    override fun hashCode(): Int {
        return subscriptionId?.hashCode() ?: 0
    }
}