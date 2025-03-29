package com.backend.sodam.domain.tags.controller.response

data class TagResponse(
    val articleId: Long,
    val tagId: Long,
    val tagName: String
)
