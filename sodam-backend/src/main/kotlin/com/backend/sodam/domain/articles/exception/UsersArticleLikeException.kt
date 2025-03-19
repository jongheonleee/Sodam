package com.backend.sodam.domain.articles.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class UsersArticleLikeException(val errorCode: ErrorCode) : RuntimeException() {
    class UsersArticleLikeNotFoundException : UsersArticleLikeException(ErrorCode.USERS_ARTICLE_LIKE_NOT_FOUND)
}