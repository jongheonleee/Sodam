package com.backend.sodam.domain.articles.controller.request

data class ArticleCreateRequest(
    val title: String,
    val summary: String,
    val content: String,
    val tags: List<String>,
)
