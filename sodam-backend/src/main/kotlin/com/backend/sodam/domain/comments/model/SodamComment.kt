package com.backend.sodam.domain.comments.model

class SodamComment(
    val commentId: Long,
    val userId: String,
    val articleId: Long,
    var profileImageUrl: String,
    var userName: String,
    val createdAt: String,
    var content: String,
    var commentLikeCnt: Long,
    var commentDislikeCnt: Long,
) {

    fun canAccess(userId: String): Boolean {
        return this.userId == userId
    }
}