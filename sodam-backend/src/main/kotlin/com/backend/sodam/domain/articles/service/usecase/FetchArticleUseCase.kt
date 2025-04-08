package com.backend.sodam.domain.articles.service.usecase

import com.backend.sodam.domain.articles.controller.response.ArticleDetailResponse
import com.backend.sodam.domain.articles.controller.response.ArticleSimpleResponse
import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FetchArticleUseCase {
    fun fetchFromClient(pageable: Pageable, articleSearchCommand: ArticleSearchCommand): Page<ArticleSummaryResponse>
    fun getArticleDetail(articleId: Long): ArticleDetailResponse
    fun getArticleSimple(userId: String, articleId: Long): ArticleSimpleResponse
}