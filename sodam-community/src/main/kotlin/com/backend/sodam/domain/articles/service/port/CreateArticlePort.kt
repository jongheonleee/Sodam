package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.users.model.UserType

interface CreateArticlePort {
    fun isTarget(userType: UserType): Boolean
    fun createArticle(userId: String, articleCreateCommand: ArticleCreateCommand): SodamArticle
}
