package com.backend.sodam.domain.comments.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class CommentException(val errorCode: ErrorCode) : RuntimeException() {
    class CommentNotFoundException : CommentException(ErrorCode.COMMENT_NOT_FOUND)
    class CommentAccessDeniedException : CommentException(ErrorCode.COMMENT_ACCESS_DENIAL)
    class UserLikeCommentNotFoundException : CommentException(ErrorCode.USERS_COMMENT_LIKE_NOT_FOUND)
    class UserDislikeCommentNotFoundException : CommentException(ErrorCode.USERS_COMMENT_DISLIKE_NOT_FOUND)
}
