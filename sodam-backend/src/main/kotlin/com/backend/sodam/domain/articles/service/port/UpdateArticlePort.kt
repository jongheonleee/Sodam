package com.backend.sodam.domain.articles.service.port

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand

interface UpdateArticlePort {
    fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): SodamArticle
    fun increaseViewCnt(articleId: Long)
}