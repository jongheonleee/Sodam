package com.backend.sodam.domain.articles.model

class SodamArticle(
    val articleId: Long,
    val title: String,
    val author: String,
    val summary: String,
    val content: String,
    var tags: List<String>,
    var viewCnt: Int,
    var likeCnt: Int,
    var dislikeCnt: Int,
    val createdAt: String,
) {
}