package com.backend.sodam.domain.articles.service.usecase

import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand
import com.backend.sodam.domain.articles.service.response.ArticleUpdateResponse

interface UpdateArticleUseCase {
    fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): ArticleUpdateResponse
}
