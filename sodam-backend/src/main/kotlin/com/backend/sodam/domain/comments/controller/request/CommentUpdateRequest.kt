package com.backend.sodam.domain.comments.controller.request

import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand

data class CommentUpdateRequest(
    val comment: String
) {
    fun toCommand(userId: String): CommentUpdateCommand {
        return CommentUpdateCommand(
            userId = userId,
            content = comment
        )
    }
}
