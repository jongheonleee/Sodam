package com.backend.sodam.domain.tags.service.response

data class TagResponse(
    val articleId: Long,
    val tagId: Long,
    val tagName: String,
)
