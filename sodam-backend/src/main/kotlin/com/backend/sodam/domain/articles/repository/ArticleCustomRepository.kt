package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.model.SodamDetailArticle
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ArticleCustomRepository {
    fun findByPageBy(pageRequest: Pageable, articleSearchCommand: ArticleSearchCommand): Page<SodamArticle>
    fun findDetailByArticleId(articleId: Long): SodamDetailArticle
}
