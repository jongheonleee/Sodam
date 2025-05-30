package sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.controller

import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.controller.request.OrdersHistoryRequest
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.controller.request.QuerySearch
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity.OrdersHistoryEntity
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository.OrdersHistoryNativeRepository
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository.OrdersHistoryRepository
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository.reponse.SearchResponse

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping("/order-history")
class OrderHistoryController(
    private val repository: OrdersHistoryRepository,
    private val nativeRepository: OrdersHistoryNativeRepository,
) {

    @GetMapping("/{orderId}")
    suspend fun get(@PathVariable("orderId") orderId: String): OrdersHistoryEntity? {
        return repository.findById(orderId)
    }

    @GetMapping("/all")
    suspend fun getAll(): Flow<OrdersHistoryEntity> {
        return repository.findAll()
    }

    @PostMapping
    suspend fun save(@RequestBody request: OrdersHistoryRequest): OrdersHistoryEntity {
        logger.debug { ">> got request ${request.toString()}" }
        val document = repository.findById(request.orderId)?.let { orderHistory ->
            request.userId?.let { orderHistory.userId = it }
            request.socialUserId?.let { orderHistory.socialUserId = it }
            request.orderTotAmount?.let { orderHistory.orderTotAmount = it }
            request.discTotAmount?.let { orderHistory.discTotAmount = it }
            request.paidTotAmount?.let { orderHistory.paidTotAmount = it }
            request.description?.let { orderHistory.description = it }
            request.pgStatus?.let { orderHistory.pgStatus = it }
            request.orderedAt?.let { orderHistory.orderedAt = it }
            request.createdAt.let { orderHistory.createdAt = it }
            request.modifiedAt.let { orderHistory.modifiedAt = it }

            orderHistory
        } ?: request.toEntity()

        return repository.save(document)
    }

    @DeleteMapping("/{orderId}")
    suspend fun delete(@PathVariable orderId: String) {
        return repository.deleteById(orderId)
    }

    @DeleteMapping("/all")
    suspend fun deleteAll() {
        return repository.deleteAll()
    }

    @GetMapping("/search")
    suspend fun search(request: QuerySearch): SearchResponse {
        return nativeRepository.search(request)
    }
}
