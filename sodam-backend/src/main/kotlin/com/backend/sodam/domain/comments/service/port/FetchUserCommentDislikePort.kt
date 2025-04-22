package com.backend.sodam.domain.comments.service.port

import com.backend.sodam.domain.users.model.UserType

interface FetchUserCommentDislikePort {
    fun isTarget(userType: UserType): Boolean
    fun existsCommentDislike(commentId: Long, userId: String): Boolean
}
