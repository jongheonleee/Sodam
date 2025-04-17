package com.backend.sodam.domain.secrets.service.usecase

import com.backend.sodam.domain.secrets.controller.response.SecretDetailResponse
import com.backend.sodam.domain.secrets.controller.response.SecretSummaryResponse
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FetchSecretUseCase {
    fun getSecretDetail(userId: String, secretId: Long, role: String): SecretDetailResponse
    fun fetchFromClient(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SecretSummaryResponse>
}