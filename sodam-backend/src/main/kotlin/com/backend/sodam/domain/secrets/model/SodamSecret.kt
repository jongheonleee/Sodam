package com.backend.sodam.domain.secrets.model

class SodamSecret(
    val secretId: Long,
    val username: String,
    val title: String,
    val summary: String,
    val createdAt: String,
    val thumbnailUrl: String,
    val tags: List<String>
)
