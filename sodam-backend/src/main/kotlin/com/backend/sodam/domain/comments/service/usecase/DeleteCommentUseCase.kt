package com.backend.sodam.domain.comments.service.usecase

interface DeleteCommentUseCase {
    fun delete(userId: String, commentId: Long)
}