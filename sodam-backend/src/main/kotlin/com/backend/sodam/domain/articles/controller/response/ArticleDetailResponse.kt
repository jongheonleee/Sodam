package com.backend.sodam.domain.articles.controller.response

import com.backend.sodam.domain.comments.controller.response.CommentResponse
import com.backend.sodam.domain.tags.controller.response.TagResponse

data class ArticleDetailResponse(
    val userId: String,
    val articleId: Long,
    val title: String,
    val profileImageUrl: String,
    val author: String,
    val content: String,
    val createdAt: String,
    val tags: List<TagResponse>,
    val comments: List<CommentResponse>,
    val images: List<String>, // 추후에 ImageResponse로 처리
    val articleLikeCnt: Long,
    val articleDislikeCnt: Long,
    val articleViewCnt: Long
)
