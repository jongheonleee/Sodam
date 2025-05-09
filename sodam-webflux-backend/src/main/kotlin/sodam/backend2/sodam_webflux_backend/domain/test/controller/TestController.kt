package sodam.backend2.sodam_webflux_backend.domain.test.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import kotlinx.coroutines.delay
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import sodam.backend2.sodam_webflux_backend.domain.test.controller.request.CreateTestArticleRequest
import sodam.backend2.sodam_webflux_backend.domain.test.controller.request.ErrorTestRequest
import sodam.backend2.sodam_webflux_backend.domain.test.model.TestArticle
import sodam.backend2.sodam_webflux_backend.domain.test.service.TestService
import sodam.backend2.sodam_webflux_backend.golbal.annotation.DateString
import sodam.backend2.sodam_webflux_backend.golbal.exception.InvalidParameter

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

    // WebFlux에서는 BindingResult 지원하지 않음 -> 구현해서 사용
    @PutMapping("/test/error")
    suspend fun error(@RequestBody @Valid request: ErrorTestRequest) {
        logger.debug { "request: ${request}" }

        if (request.message == "error")  // 이런 에러 메시지를 내포한 경우, 파라미터 에러로 처리해야함
            throw InvalidParameter(request, request::message, code = "custom code", message = "custom message")

//        throw RuntimeException("error!!")
    }
}



