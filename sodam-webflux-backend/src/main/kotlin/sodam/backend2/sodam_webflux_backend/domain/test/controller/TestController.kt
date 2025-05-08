package sodam.backend2.sodam_webflux_backend.domain.test.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.KotlinDetector
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import sodam.backend2.sodam_webflux_backend.domain.test.controller.request.CreateTestArticleRequest
import sodam.backend2.sodam_webflux_backend.domain.test.model.TestArticle
import sodam.backend2.sodam_webflux_backend.domain.test.service.TestService
import kotlin.coroutines.Continuation

private val logger = KotlinLogging.logger {}


@RestController
class TestController(
    private val service: TestService,
) {

    @GetMapping("/test/mdc")
    suspend fun testTxid() {
        logger.debug { "start MDC Txid!!" }
        delay(100) // event loop에서 잠들었다가 깨어나오고 다른 스레드에서 실행됨
        service.mdc1()
        logger.debug { "end MDC Txid!!" }
    }

    @GetMapping("/test/mdc2")
    fun testAnother() {
        logger.debug { "test another!!" }
    }

    @PostMapping("/test-article")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createTestArticle(@RequestBody request: CreateTestArticleRequest): TestArticle {
        return service.create(request)
    }
}

