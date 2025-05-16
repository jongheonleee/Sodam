package com.backend.sodam.domain.comments.service.usecase

interface HandleCommentDislikeUseCase {
    fun handleDislike(commentId: Long, userId: String)
}
