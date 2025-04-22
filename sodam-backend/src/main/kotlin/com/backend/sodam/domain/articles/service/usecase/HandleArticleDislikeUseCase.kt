package com.backend.sodam.domain.articles.service.usecase

interface HandleArticleDislikeUseCase {
    fun handleDislike(userId: String, articleId: Long)
}
