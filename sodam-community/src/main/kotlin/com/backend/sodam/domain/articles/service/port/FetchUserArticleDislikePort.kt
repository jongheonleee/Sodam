package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.users.model.UserType

interface FetchUserArticleDislikePort {
    fun isTarget(userType: UserType): Boolean
    fun existsArticleDislike(articleId: Long, userId: String): Boolean
}
