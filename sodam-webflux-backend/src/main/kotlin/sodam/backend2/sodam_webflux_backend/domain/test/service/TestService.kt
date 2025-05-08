package sodam.backend2.sodam_webflux_backend.domain.test.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import sodam.backend2.sodam_webflux_backend.domain.test.controller.request.CreateTestArticleRequest
import sodam.backend2.sodam_webflux_backend.domain.test.model.TestArticle
import sodam.backend2.sodam_webflux_backend.domain.test.repository.TestRepository


private val logger = KotlinLogging.logger {}

@Service
class TestService(
    private val repository: TestRepository,
) {

    suspend fun mdc1() {
        logger.debug { "start mdc1" }
        mdc2()
        logger.debug { "end mdc1" }
    }

    suspend fun mdc2() {
        logger.debug { "start mdc2" }
        delay(100)
        repository.findById(1).let {
            logger.debug { "test: $it" }
        }

        Mono.fromCallable {
            logger.debug { "reactor call!! " }
        }.subscribeOn(Schedulers.boundedElastic()).awaitSingle()

        logger.debug { "end mdc2" }
    }

    suspend fun create(request: CreateTestArticleRequest): TestArticle {
        return repository.save(request.toTestArticle())
    }
}