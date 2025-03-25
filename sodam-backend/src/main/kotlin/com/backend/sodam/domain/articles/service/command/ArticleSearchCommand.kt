package com.backend.sodam.domain.articles.service.command

data class ArticleSearchCommand(
    val keyword: String? = null,
    val tag: String? = null,
    val categoryId: String? = null,
)
