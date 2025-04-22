package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.users.model.UserType

interface FetchUserArticleLikePort {
    fun isTarget(userType: UserType): Boolean
    fun existsArticleLike(articleId: Long, userId: String): Boolean
}
