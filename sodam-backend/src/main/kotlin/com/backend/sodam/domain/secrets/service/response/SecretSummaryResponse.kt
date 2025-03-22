package com.backend.sodam.domain.secrets.service.response

data class SecretSummaryResponse(
    val secretId: Long,
    val username: String,
    val profileImageUrl: String = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", // aws s3 적용하면 바꿀 예정
    val title: String,
    val summary: String,
    val createdAt: String,
    val thumbnailUrl: String,
    val tags: List<String>
)
