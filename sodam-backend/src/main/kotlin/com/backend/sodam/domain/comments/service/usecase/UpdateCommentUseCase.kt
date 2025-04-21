package com.backend.sodam.domain.comments.service.usecase

import com.backend.sodam.domain.comments.service.response.CommentUpdateResponse
import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand

interface UpdateCommentUseCase {
    fun update(commentId: Long, commentUpdateCommand: CommentUpdateCommand): CommentUpdateResponse
}