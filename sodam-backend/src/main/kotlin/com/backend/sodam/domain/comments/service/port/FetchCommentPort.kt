package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.comments.model.SodamComment

interface FetchCommentPort {
    fun findByCommentId(commentId: Long): SodamComment
    fun isExistsComment(commentId: Long): Boolean
}