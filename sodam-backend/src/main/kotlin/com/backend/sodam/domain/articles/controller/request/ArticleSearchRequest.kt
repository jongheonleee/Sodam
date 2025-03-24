package com.backend.sodam.domain.articles.controller.request

import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import org.springframework.web.bind.annotation.RequestParam

data class ArticleSearchRequest(
    @RequestParam
    val title: String? = null,
    @RequestParam
    val author: String? = null,
    @RequestParam
    val tag: String? = null,
    @RequestParam
    val categoryId: String? = null,
) {
    fun toCommand(): ArticleSearchCommand {
        return ArticleSearchCommand(
            title = title,
            author = author,
            tag = tag,
            categoryId = categoryId,
        )
    }
}
