package com.backend.sodam.domain.articles.model

class SodamArticle(
    val userId: String,
    val articleId: Long,
    val title: String,
    val author: String,
    val summary: String,
    val content: String,
    var tags: List<String>,
    var viewCnt: Long,
    var likeCnt: Long,
    var dislikeCnt: Long,
    val createdAt: String
) {

    fun canAccess(userId: String): Boolean = this.userId == userId
}
