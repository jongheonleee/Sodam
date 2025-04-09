package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.comments.model.SodamComment
import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand

interface UpdateCommentPort {
    fun update(commentId: Long, commentUpdateCommand: CommentUpdateCommand): SodamComment
}