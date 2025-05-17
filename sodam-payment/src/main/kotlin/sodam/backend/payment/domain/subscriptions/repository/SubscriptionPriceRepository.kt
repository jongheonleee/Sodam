package sodam.backend.payment.domain.subscriptions.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionPriceEntity


@Repository
interface SubscriptionPriceRepository: CoroutineCrudRepository<SubscriptionPriceEntity, Long> {
}