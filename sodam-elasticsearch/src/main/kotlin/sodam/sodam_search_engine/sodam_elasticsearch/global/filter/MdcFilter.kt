package sodam.sodam_search_engine.sodam_elasticsearch.global.filter

import io.micrometer.context.ContextRegistry
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.util.context.Context
import sodam.sodam_search_engine.sodam_elasticsearch.global.extension.txid
import java.util.UUID

private val logger = KotlinLogging.logger {}

const val KEY_TXID = "txid"


@Component
@Order(1)
class MdcFilter: WebFilter {

    init {
        propagateMdcThroughReactor()
    }

    private fun propagateMdcThroughReactor() {
        Hooks.enableAutomaticContextPropagation() // publisher 간에 컨텍스트를 복사함
        ContextRegistry.getInstance().registerThreadLocalAccessor(
            KEY_TXID,
            { MDC.get(KEY_TXID) },
            { value -> MDC.put(KEY_TXID, value) },
            { MDC.remove(KEY_TXID) },
        )
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val uuid = exchange.request.headers["x-txid"]?.firstOrNull() ?: "${UUID.randomUUID()}" // 기존께 있으면 쓰고 없으면 발급하게 만듦
        MDC.put(KEY_TXID, uuid)
        logger.debug { "request id: ${exchange.request.id}" }
        return chain.filter(exchange).contextWrite {
            Context.of(KEY_TXID, uuid)
        }.doOnError {
            exchange.request.txid = uuid
        }
    }
}