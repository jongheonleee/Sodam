package com.backend.sodam.domain.articles.service.command

data class ArticleSearchCommand(
    val title: String? = null,
    val author: String? = null,
    val tag: String? = null,
    val categoryId: String? = null,
)
