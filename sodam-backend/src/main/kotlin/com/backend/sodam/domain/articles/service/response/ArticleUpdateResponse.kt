package com.backend.sodam.domain.articles.service.response

data class ArticleUpdateResponse(
    val articleId: Long,
    val title: String,
    val author: String,
    val summary: String,
    val content: String,
    val tags: List<String>,
    val createdAt: String
)
