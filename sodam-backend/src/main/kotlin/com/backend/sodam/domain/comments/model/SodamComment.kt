package com.backend.sodam.domain.comments.model

class SodamComment(
    val commentId: Long,
    val articleId: Long,
    var profileImageUrl: String,
    var userName: String,
    val createdAt: String,
    var content: String,
    var commentLikeCnt: Long,
    var commentDislikeCnt: Long,
) {
}