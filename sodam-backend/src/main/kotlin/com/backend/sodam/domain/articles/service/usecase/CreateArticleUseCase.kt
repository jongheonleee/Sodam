package com.backend.sodam.domain.articles.service.usecase

import com.backend.sodam.domain.articles.controller.response.ArticleCreateResponse
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand

interface CreateArticleUseCase {
    fun create(userId: String, articleCreateCommand: ArticleCreateCommand): ArticleCreateResponse
}