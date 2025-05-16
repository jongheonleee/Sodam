package com.backend.sodam.domain.comments.service.usecase

import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand
import com.backend.sodam.domain.comments.service.response.CommentUpdateResponse

interface UpdateCommentUseCase {
    fun update(commentId: Long, commentUpdateCommand: CommentUpdateCommand): CommentUpdateResponse
}
