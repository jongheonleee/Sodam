package com.backend.sodam.domain.articles.controller.request

data class ArticleUpdateRequest(
    val title: String,
    val summary: String,
    val content: String,
    val tags: List<String>,
)
