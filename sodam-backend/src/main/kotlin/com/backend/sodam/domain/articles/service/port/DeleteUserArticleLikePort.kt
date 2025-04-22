package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.users.model.UserType

interface DeleteUserArticleLikePort {
    fun isTarget(userType: UserType): Boolean
    fun deleteLike(articleId: Long, userId: String)
}
