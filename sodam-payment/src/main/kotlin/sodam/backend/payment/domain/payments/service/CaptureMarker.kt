package sodam.backend.payment.domain.payments.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.service.OrderService

@Service
class CaptureMarker(
    private val template: ReactiveRedisTemplate<Any, Any>,
    @Value("\${spring.profiles.active:local}")
    private val profile: String,
    private val orderService: OrderService,
) {

    private val ops = template.opsForSet()
    private val key = "$profile/capture-marker"

    suspend fun put(orderId: String) {
        ops.add(key, orderId).awaitFirstOrNull()
    }

    suspend fun remove(orderId: String) {
        ops.remove(key, orderId).awaitFirstOrNull()
    }

    suspend fun getAll(): List<OrdersEntity> {
        return ops.members(key)
                  .asFlow()
                  .map { orderService.get(it as String) }
                  .toList()
                  .sortedBy { it.modifiedAt }
    }
}