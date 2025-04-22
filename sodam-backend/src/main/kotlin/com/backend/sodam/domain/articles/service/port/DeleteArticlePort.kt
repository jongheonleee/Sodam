package com.backend.sodam.domain.articles.service.port

interface DeleteArticlePort {
    fun delete(articleId: Long)
}
