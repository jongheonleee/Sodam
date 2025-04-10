package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.users.model.UserType

interface CreateUserCommentLikePort {
    fun isTarget(userType: UserType): Boolean
    fun createLike(commentId: Long, userId: String)
}