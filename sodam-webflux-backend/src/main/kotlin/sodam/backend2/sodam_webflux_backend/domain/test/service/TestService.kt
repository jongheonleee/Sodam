package sodam.backend2.sodam_webflux_backend.domain.test.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import sodam.backend2.sodam_webflux_backend.domain.test.controller.request.CreateTestArticleRequest
import sodam.backend2.sodam_webflux_backend.domain.test.model.TestArticle
import sodam.backend2.sodam_webflux_backend.domain.test.repository.TestRepository
import sodam.backend2.sodam_webflux_backend.golbal.annotation.DateString
import sodam.backend2.sodam_webflux_backend.golbal.extension.toLocalDate
import java.time.LocalDateTime


private val logger = KotlinLogging.logger {}

@Service
class TestService(
    private val repository: TestRepository,
    private val dbClient: DatabaseClient,
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

    suspend fun getAll(request: QueryArticleTest): Flow<TestArticle> {
        val params = HashMap<String, Any>()
        var sql = dbClient.sql("""
            SELECT 
                id, title, body, author_id, created_at, updated_at
            FROM TB_ARTICLE
            WHERE  1=1 
            ${
                request.title.query { 
                    params["title"] = it.trim().let { "%$it" }
                    "AND title LIKE :title"
                }
            }
            ${ 
                request.authorId.query {
                    params["authorId"] = it
                    "AND author_id IN (:authorId)"
                }
            }
            ${
                request.from.query {
                    params["authorId"] = it.toLocalDate()
                    "AND created_at >= :from"
                }
            }
            ${
                request.to.query {
                    params["to"] = it.toLocalDate().plusDays(1)
                    // 2025-05-12 -> 2025-05-13 00:00:00.000
                    // <= -> < 변환 
                    "AND created_at < :to"
                }
            }
        """.trimIndent())

        params.forEach { key, value -> sql = sql.bind(key, value) }

        return sql.map { row ->
            TestArticle(
                id        = row.get("id") as Long,
                title     = row.get("title") as String,
                body      = row.get("body") as String?,
                authorId  = row.get("author_id") as Long,
            ).apply {
                createdAt = row.get("created_at") as LocalDateTime?
                updatedAt = row.get("updated_at") as LocalDateTime?
            }
        }.flow()
    }
}

fun <T> T?.query(f: (T) -> String): String {
    return when {
        this == null -> ""
        this is String && this.isBlank() -> ""
        this is Collection<*> && this.isEmpty() -> ""
        this is Array<*> && this.isEmpty() -> ""
        else -> f.invoke(this)
    }
}

data class QueryArticleTest(
    val title: String?,
    val authorId: List<Long>?,

    @DateString
    val from: String?,

    @DateString
    val to: String?,
)