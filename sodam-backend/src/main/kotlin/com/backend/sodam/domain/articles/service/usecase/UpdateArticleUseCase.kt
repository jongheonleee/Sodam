package com.backend.sodam.domain.articles.service.usecase

import com.backend.sodam.domain.articles.controller.response.ArticleUpdateResponse
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand

interface UpdateArticleUseCase {
    fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): ArticleUpdateResponse
}