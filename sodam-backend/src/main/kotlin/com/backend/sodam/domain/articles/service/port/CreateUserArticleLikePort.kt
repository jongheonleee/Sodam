package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.users.model.UserType

interface CreateUserArticleLikePort {
    fun isTarget(userType: UserType): Boolean
    fun createLike(userId: String, articleId: Long)
}