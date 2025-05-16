package com.backend.sodam.domain.comments.service.usecase

import com.backend.sodam.domain.comments.service.response.CommentSimpleResponse

interface FetchCommentUseCase {
    fun getSimpleComment(commentId: Long): CommentSimpleResponse
}
