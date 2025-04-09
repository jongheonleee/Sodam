package com.backend.sodam.domain.comments.service.usecase

import com.backend.sodam.domain.comments.controller.response.CommentSimpleResponse

interface FetchCommentUseCase {
    fun getSimpleComment(commentId: Long): CommentSimpleResponse
}