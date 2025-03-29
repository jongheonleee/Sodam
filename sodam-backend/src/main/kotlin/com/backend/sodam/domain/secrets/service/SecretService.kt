package com.backend.sodam.domain.secrets.service

import com.backend.sodam.domain.secrets.repository.SecretRepository
import com.backend.sodam.domain.secrets.service.command.SecretCreateCommand
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import com.backend.sodam.domain.secrets.controller.response.SecretCreateResponse
import com.backend.sodam.domain.secrets.controller.response.SecretDetailResponse
import com.backend.sodam.domain.secrets.controller.response.SecretSummaryResponse
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SecretService(
    private val secretRepository: SecretRepository
) {

    fun create(secretCreateCommand: SecretCreateCommand): SecretCreateResponse {
        return SecretCreateResponse(
            10
        )
    }

    fun fetchFromClient(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SecretSummaryResponse> {
        return secretRepository.findByPageBy(
            pageable = pageable,
            secretSearchCommand = secretSearchCommand
        ).map {
            SecretSummaryResponse(
                secretId = it.secretId,
                username = it.username,
                title = it.title,
                summary = it.summary,
                createdAt = it.createdAt,
                thumbnailUrl = it.thumbnailUrl,
                tags = it.tags
            )
        }
    }

    fun getSecretDetail(secretId: Long): SecretDetailResponse {
        secretRepository.increaseViewCnt(secretId)
        val sodamDetailSecret = secretRepository.findDetailBySecretId(secretId)
        return SecretDetailResponse(
            secretId = sodamDetailSecret.secretId,
            author = sodamDetailSecret.author,
            title = sodamDetailSecret.title,
            content = sodamDetailSecret.content,
            createdAt = sodamDetailSecret.createdAt,
            tags = sodamDetailSecret.tags,
            comments = sodamDetailSecret.comments,
            images = sodamDetailSecret.images,
            secretLikeCnt = sodamDetailSecret.secretLikeCnt,
            secretDislikeCnt = sodamDetailSecret.secretDislikeCnt,
            secretViewCnt = sodamDetailSecret.secretViewCnt
        )
    }
}
