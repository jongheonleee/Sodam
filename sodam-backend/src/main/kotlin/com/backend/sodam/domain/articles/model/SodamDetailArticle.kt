package com.backend.sodam.domain.articles.model

import com.backend.sodam.domain.articles.service.response.ArticleDetailResponse
import com.backend.sodam.domain.comments.service.response.CommentResponse
import com.backend.sodam.domain.tags.controller.response.TagResponse

class SodamDetailArticle(
    val userId: String,
    val articleId: Long,
    val title: String,
    val profileImageUrl: String = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    val author: String,
    val content: String,
    val createdAt: String,
    val tags: List<TagResponse>,
    val comments: List<CommentResponse>,
    val images: List<String> = listOf(), // 추후에 ImageResponse로 처리
    val articleLikeCnt: Long,
    val articleDislikeCnt: Long,
    val articleViewCnt: Long
) {
    fun toResponse(): ArticleDetailResponse {
        return ArticleDetailResponse(
            userId = this.userId,
            articleId = this.articleId,
            title = this.title,
            profileImageUrl = this.profileImageUrl,
            author = this.author,
            content = this.content,
            createdAt = this.createdAt,
            tags = this.tags,
            comments = this.comments,
            images = this.images,
            articleLikeCnt = this.articleLikeCnt,
            articleDislikeCnt = this.articleDislikeCnt,
            articleViewCnt = this.articleViewCnt
        )
    }
}
