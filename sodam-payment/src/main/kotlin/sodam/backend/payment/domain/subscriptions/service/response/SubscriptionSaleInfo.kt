package sodam.backend.payment.domain.subscriptions.service.response

import java.io.Serializable

data class SubscriptionSaleInfo(
    val subscriptionId: String,
    val subscriptionName: String,
    val subscriptionContent: String,
    val viewCnt: Int,
    val downCnt: Int,
    val price: Long, // 원가
    val discPrice: Long, // 할인 금액
    val discRate: Float, // 할인율
    val salePrice: Long, // 최종 판매 금액
    val isSale: Boolean,
): Serializable {
    override fun toString(): String {
        return "SubscriptionSaleInfo(subscriptionId='$subscriptionId', subscriptionName='$subscriptionName', subscriptionContent='$subscriptionContent', viewCnt=$viewCnt, downCnt=$downCnt, price=$price, discPrice=$discPrice, discRate=$discRate, salePrice=$salePrice)"
    }
}
