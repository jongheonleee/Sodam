package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.users.model.UserType

interface FetchUserCommentLikePort {
    fun isTarget(userType: UserType): Boolean
    fun existsCommentLike(commentId: Long, userId: String): Boolean
}