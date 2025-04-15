package com.backend.sodam.domain.secrets.service

import com.backend.sodam.domain.secrets.controller.response.SecretCreateResponse
import com.backend.sodam.domain.secrets.controller.response.SecretDetailResponse
import com.backend.sodam.domain.secrets.controller.response.SecretSummaryResponse
import com.backend.sodam.domain.secrets.exception.SecretException
import com.backend.sodam.domain.secrets.repository.SecretRepository
import com.backend.sodam.domain.secrets.repository.SecretViewRepository
import com.backend.sodam.domain.secrets.service.command.SecretCreateCommand
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class SecretService(
    private val secretRepository: SecretRepository,
    private val secretViewRepository: SecretViewRepository,
    private val viewValidators: List<SecretViewValidator>
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

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    fun getSecretDetail(userId: String, secretId: Long, role: String): SecretDetailResponse {
        val totalViewCnt = secretViewRepository.countViewToday(userId = userId) // 보유 구독권 서비스에서 현재 회원의 당일 조회수 확인
        val isViewable = viewValidators.stream()
            .filter { it.isTarget(role) } // 현재 발급된 구독권
            .findFirst()
            .orElseThrow()
            .isValidView(totalViewCnt) // 조회 가능 여부 확인

        if ( ! isViewable )
            throw SecretException.InvalidSecretViewException()


        secretRepository.increaseViewCnt(secretId) // 조회수 증가
        secretViewRepository.create(userId = userId, secretId = secretId) // 시청 이력 생성
        return secretRepository.findDetailBySecretId(secretId = secretId)
                               .toResponse()
    }
}
