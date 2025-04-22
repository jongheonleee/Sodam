package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.users.model.UserType

interface CreateUserArticleDislikePort {
    fun isTarget(userType: UserType): Boolean
    fun createDislike(userId: String, articleId: Long)
}
