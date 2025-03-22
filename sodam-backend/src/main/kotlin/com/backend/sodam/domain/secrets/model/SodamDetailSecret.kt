package com.backend.sodam.domain.secrets.model

import com.backend.sodam.domain.comments.service.response.CommentResponse
import com.backend.sodam.domain.tags.service.response.TagResponse

class SodamDetailSecret(
    val secretId: Long,
    val author: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val tags: List<TagResponse>,
    val comments: List<CommentResponse> = listOf(), // 추후에 추가
    val images: List<String> = listOf(),
    val secretViewCnt: Long,
    val secretLikeCnt: Long,
    val secretDislikeCnt: Long
)
