package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.users.model.UserType

interface CreateUserCommentDislikePort {
    fun isTarget(userType: UserType): Boolean
    fun createDislike(userId: String, commentId: Long)
}
