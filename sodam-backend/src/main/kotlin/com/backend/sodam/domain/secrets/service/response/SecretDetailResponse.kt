package com.backend.sodam.domain.secrets.service.response

import com.backend.sodam.domain.comments.service.response.CommentResponse
import com.backend.sodam.domain.tags.service.response.TagResponse

data class SecretDetailResponse(
    val secretId: Long,
    val author: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val tags: List<TagResponse>,
    val comments: List<CommentResponse>,
    val images: List<String>,
    val secretLikeCnt: Long,
    val secretDislikeCnt: Long,
    val secretViewCnt: Long
)
