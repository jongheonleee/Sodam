package com.backend.sodam.domain.articles.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class UsersArticleDislikeException(val errorCode: ErrorCode) : RuntimeException() {
    class UsersArticleDislikeNotFoundException : UsersArticleDislikeException(ErrorCode.USERS_ARTICLE_DISLIKE_NOT_FOUND)
}