package com.backend.sodam.domain.secrets.model

import com.backend.sodam.domain.secrets.controller.response.SecretSummaryResponse

class SodamSecret(
    val secretId: Long,
    val username: String,
    val title: String,
    val summary: String,
    val createdAt: String,
    val thumbnailUrl: String,
    val tags: List<String>
) {
    fun toSummaryResponse(): SecretSummaryResponse {
        return SecretSummaryResponse(
            secretId = this.secretId,
            username = this.username,
            title = this.title,
            summary = this.summary,
            createdAt = this.createdAt,
            thumbnailUrl = this.thumbnailUrl,
            tags = this.tags
        )
    }
}
