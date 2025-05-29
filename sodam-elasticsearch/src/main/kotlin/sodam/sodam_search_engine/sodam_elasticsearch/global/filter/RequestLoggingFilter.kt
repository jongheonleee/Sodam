package sodam.sodam_search_engine.sodam_elasticsearch.global.filter

import mu.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

private val logger = KotlinLogging.logger {}

@Order(11) // MdcFilter 보다 늦게 로딩 설정
@Component
class RequestLoggingFilter: WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        // 요청 uri, method, ip 로그로 기록
        logger.info { "uri : [${request.method}] ${request.path}, ip: ${request.remoteAddress}" }

        // header, query, body 로그로 기록
        if (request.headers.isNotEmpty()) {
            logger.info("header: ${request.headers}")
        }

        if (request.queryParams.isNotEmpty())
            logger.info { "query : ${request.queryParams}" }

        val loggingRequest =  object: ServerHttpRequestDecorator(request) {
            override fun getBody(): Flux<DataBuffer> {
                return super.getBody().doOnNext { buffer ->
                    ByteArrayOutputStream().use { output ->
                        Channels.newChannel(output).write(buffer.readableByteBuffers().next())
                        String(output.toByteArray())
                    }.let { requestBody ->
                        logger.debug { "payload: $requestBody" }
                    }
                }
            }
        }

        return chain.filter(
            exchange.mutate().request(loggingRequest).build()
        )
    }
}