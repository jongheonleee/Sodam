package com.backend.sodam.domain.comments.model

import com.backend.sodam.domain.comments.controller.response.CommentCreateResponse
import com.backend.sodam.domain.comments.controller.response.CommentSimpleResponse
import com.backend.sodam.domain.comments.controller.response.CommentUpdateResponse

class SodamComment(
    val commentId: Long,
    val userId: String,
    val articleId: Long,
    var profileImageUrl: String,
    var userName: String,
    val createdAt: String,
    var content: String,
    var commentLikeCnt: Long,
    var commentDislikeCnt: Long
) {

    fun canAccess(userId: String): Boolean {
        return this.userId == userId
    }

    fun toCreateResponse(): CommentCreateResponse {
        return CommentCreateResponse(
            commentId = this.commentId,
            articleId = this.articleId,
            profileImageUrl = this.profileImageUrl,
            userName = this.userName,
            createdAt = this.createdAt,
            content = this.content,
            commentLikeCnt = this.commentLikeCnt,
            commentDislikeCnt = this.commentDislikeCnt
        )
    }

    fun toSimpleResponse(): CommentSimpleResponse {
        return CommentSimpleResponse(
            commentId = this.commentId,
            comment = this.content
        )
    }

    fun toUpdateResponse(): CommentUpdateResponse {
        return CommentUpdateResponse(
            commentId = this.commentId,
            comment = this.content
        )
    }
}
