package com.backend.sodam.domain.comments.service.usecase

import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.comments.service.response.CommentCreateResponse

interface CreateCommentUseCase {
    fun create(articleId: Long, commentCreateCommand: CommentCreateCommand): CommentCreateResponse
}
