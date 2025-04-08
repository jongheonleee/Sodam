package com.backend.sodam.domain.articles.service.usecase

interface HandleArticleLikeUseCase {
    fun handleLike(userId: String, articleId: Long)
}