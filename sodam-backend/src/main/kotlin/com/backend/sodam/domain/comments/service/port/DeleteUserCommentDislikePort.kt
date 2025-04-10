package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.users.model.UserType

interface DeleteUserCommentDislikePort {
    fun isTarget(userType: UserType): Boolean
    fun deleteDislike(commentId: Long, userId: String)
}