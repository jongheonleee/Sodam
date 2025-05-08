package sodam.backend2.sodam_webflux_backend.domain.test.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend2.sodam_webflux_backend.domain.test.model.TestArticle

@Repository
interface TestRepository: CoroutineCrudRepository<TestArticle, Long> {
    suspend fun findAlByTitleContains(title: String): Flow<TestArticle>
}