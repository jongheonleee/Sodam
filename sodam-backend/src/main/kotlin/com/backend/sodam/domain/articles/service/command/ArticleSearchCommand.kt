package com.backend.sodam.domain.articles.service.command

import org.springframework.web.bind.annotation.RequestParam

data class ArticleSearchCommand(
    val title: String? = null,
    val author: String? = null,
    val tag: String? = null,
)
