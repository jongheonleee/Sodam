package sodam.backend2.sodam_webflux_backend.domain.test.controller.request

import sodam.backend2.sodam_webflux_backend.domain.test.model.TestArticle

data class CreateTestArticleRequest (
    val title: String,
    val body: String? = null,
    val authorId: Long? = null,
) {
    fun toTestArticle(): TestArticle {
        return TestArticle(
            title = this.title,
            body = this.body,
            authorId = this.authorId,
        )
    }
}