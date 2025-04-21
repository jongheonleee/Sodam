package com.backend.sodam.domain.comments.service.usecase

import com.backend.sodam.domain.comments.service.response.CommentCreateResponse
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand

interface CreateCommentUseCase {
    fun create(articleId: Long, commentCreateCommand: CommentCreateCommand): CommentCreateResponse
}