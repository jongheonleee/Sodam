package com.backend.sodam.domain.articles.service.command

data class ArticleUpdateCommand(
    val categoryId: String,
    val userId: String,
    val articleTitle: String,
    val articleSummary: String,
    val articleContent: String,
    val tags: List<String>,
)
