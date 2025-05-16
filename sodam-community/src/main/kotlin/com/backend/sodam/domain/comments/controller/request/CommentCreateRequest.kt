package com.backend.sodam.domain.comments.controller.request

import com.backend.sodam.domain.comments.service.command.CommentCreateCommand

data class CommentCreateRequest(
    val comment: String
) {
    fun toCommand(userId: String): CommentCreateCommand {
        return CommentCreateCommand(
            content = comment,
            userId = userId
        )
    }
}
