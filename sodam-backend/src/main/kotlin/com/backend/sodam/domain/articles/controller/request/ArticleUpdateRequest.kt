package com.backend.sodam.domain.articles.controller.request

import com.backend.sodam.domain.articles.service.response.ArticleUpdateResponse

data class ArticleUpdateRequest(
    val categoryId: String,
    val title: String,
    val summary: String,
    val content: String,
    val tags: List<String>,
) {
    fun toCommand(userId: String) : ArticleUpdateCommand {
        return ArticleUpdateCommand(

        )
    }
}
