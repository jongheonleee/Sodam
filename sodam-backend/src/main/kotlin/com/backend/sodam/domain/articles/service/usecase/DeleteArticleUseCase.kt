package com.backend.sodam.domain.articles.service.usecase

interface DeleteArticleUseCase {
    fun delete(userId: String, articleId: Long)
}