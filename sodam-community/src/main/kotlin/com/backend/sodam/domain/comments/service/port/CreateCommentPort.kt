package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.comments.model.SodamComment
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.users.model.UserType

interface CreateCommentPort {
    fun isTarget(userType: UserType): Boolean
    fun createComment(articleId: Long, commentCreateCommand: CommentCreateCommand): SodamComment
}
