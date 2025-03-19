package com.backend.sodam.domain.articles.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class ArticleException(val errorCode: ErrorCode) : RuntimeException() {
    class ArticleNotFoundException : ArticleException(ErrorCode.ARTICLE_NOT_FOUND)
    class ArticleAccessDeniedException : ArticleException(ErrorCode.ARTICLE_ACCESS_DENIAL)
}