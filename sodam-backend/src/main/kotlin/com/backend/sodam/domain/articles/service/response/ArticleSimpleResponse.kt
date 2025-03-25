package com.backend.sodam.domain.articles.service.response

data class ArticleSimpleResponse(
    val articleId: Long,
    val title: String,
    val summary: String,
    val content: String,
)
