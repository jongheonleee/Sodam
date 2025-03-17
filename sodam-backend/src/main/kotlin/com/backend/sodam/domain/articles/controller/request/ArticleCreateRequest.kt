package com.backend.sodam.domain.articles.controller.request

import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand

// 유효성 검증 애너테이션 붙이기
data class ArticleCreateRequest(
    val categoryId: String,
    val title: String,
    val summary: String,
    val content: String,
    val tags: List<String>,
) {
    fun toCommand() : ArticleCreateCommand {
        return ArticleCreateCommand(
            categoryId = categoryId,
            title = title,
            summary = summary,
            content = content,
            tags = tags
        )
    }
}
