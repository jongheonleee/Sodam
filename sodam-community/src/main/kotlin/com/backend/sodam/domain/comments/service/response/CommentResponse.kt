package com.backend.sodam.domain.comments.service.response

data class CommentResponse(
    val commentId: Long,
    val articleId: Long,
    val profileImageUrl: String,
    val userName: String,
    val createdAt: String,
    val content: String,
    val commentLikeCnt: Long,
    val commentDislikeCnt: Long
)
