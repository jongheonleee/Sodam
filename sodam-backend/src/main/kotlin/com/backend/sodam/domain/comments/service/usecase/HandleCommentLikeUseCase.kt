package com.backend.sodam.domain.comments.service.usecase

interface HandleCommentLikeUseCase {
    fun handleLike(commentId: Long, userId: String)
}