package com.backend.sodam.domain.comments.service.port

interface DeleteCommentPort {
    fun delete(commentId: Long)
}
