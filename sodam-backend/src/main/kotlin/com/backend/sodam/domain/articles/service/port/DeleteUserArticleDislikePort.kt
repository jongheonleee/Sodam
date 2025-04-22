package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.users.model.UserType

interface DeleteUserArticleDislikePort {
    fun isTarget(userType: UserType): Boolean
    fun deleteDislike(articleId: Long, userId: String)
}
