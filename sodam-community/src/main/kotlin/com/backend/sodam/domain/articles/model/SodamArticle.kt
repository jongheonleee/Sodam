package com.backend.sodam.domain.articles.model

import com.backend.sodam.domain.articles.service.response.ArticleCreateResponse
import com.backend.sodam.domain.articles.service.response.ArticleSimpleResponse
import com.backend.sodam.domain.articles.service.response.ArticleSummaryResponse
import com.backend.sodam.domain.articles.service.response.ArticleUpdateResponse

class SodamArticle(
    val userId: String,
    val articleId: Long,
    val title: String,
    val author: String,
    val summary: String,
    val content: String,
    var tags: List<String>,
    var viewCnt: Long,
    var likeCnt: Long,
    var dislikeCnt: Long,
    val createdAt: String
) {

    fun canAccess(userId: String): Boolean = this.userId == userId

    fun toSummaryResponse(): ArticleSummaryResponse {
        return ArticleSummaryResponse(
            articleId = this.articleId,
            title = this.title,
            username = this.author,
            summary = this.summary,
            createdAt = this.createdAt,
            tags = this.tags
        )
    }

    fun toArticleCreateResponse(): ArticleCreateResponse {
        return ArticleCreateResponse(
            articleId = this.articleId,
            title = this.title,
            author = this.author,
            summary = this.summary,
            content = this.content,
            tags = this.tags,
            createdAt = this.createdAt
        )
    }

    fun toArticleUpdateResponse(): ArticleUpdateResponse {
        return ArticleUpdateResponse( // 수정된 결과를 반환한다.
            articleId = this.articleId,
            title = this.title,
            author = this.author,
            summary = this.summary,
            content = this.content,
            tags = this.tags,
            createdAt = this.createdAt
        )
    }

    fun toArticleSimpleResponse(): ArticleSimpleResponse {
        return ArticleSimpleResponse(
            articleId = this.articleId,
            title = this.title,
            summary = this.summary,
            content = this.content
        )
    }
}
