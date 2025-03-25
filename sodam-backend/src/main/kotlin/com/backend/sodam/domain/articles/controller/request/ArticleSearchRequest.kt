package com.backend.sodam.domain.articles.controller.request

import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import org.springframework.web.bind.annotation.RequestParam

data class ArticleSearchRequest(
    @RequestParam
    val keyword: String? = null,
    @RequestParam
    val tag: String? = null,
    @RequestParam
    val categoryId: String? = null,
) {
    fun toCommand(): ArticleSearchCommand {
        return ArticleSearchCommand(
            keyword = keyword,
            tag = tag,
            categoryId = categoryId,
        )
    }
}
