package com.backend.sodam.domain.secrets.controller

import com.backend.sodam.domain.secrets.controller.request.SecretSearchRequest
import com.backend.sodam.domain.secrets.controller.response.SecretDetailResponse
import com.backend.sodam.domain.secrets.controller.response.SecretSummaryResponse
import com.backend.sodam.domain.secrets.service.usecase.FetchSecretUseCase
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class SecretController(
    private val fetchSecretUseCase: FetchSecretUseCase,
    private val tokenProvider: JwtTokenProvider
) {

    @GetMapping("/api/v1/secrets")
    @PreAuthorize("hasAnyRole('ROLE_FREE', 'ROLE_BRONZE', 'ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM')")
    fun getSecrets(
        pageable: Pageable,
        secretSearchRequest: SecretSearchRequest
    ): SodamApiResponse<Page<SecretSummaryResponse>> {
        val command = secretSearchRequest.toCommand()
        return SodamApiResponse.ok(
            fetchSecretUseCase.fetchFromClient(pageable, command)
        )
    }

    @GetMapping("/api/v1/secrets/{secretId}")
    @PreAuthorize("hasAnyRole('ROLE_BRONZE', 'ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM')")
    fun getSecretDetail(
        @PathVariable("secretId") secretId: Long
    ): SodamApiResponse<SecretDetailResponse> {
        val userId = tokenProvider.getUserId()
        val role = tokenProvider.getRole()
        return SodamApiResponse.ok(
            fetchSecretUseCase.getSecretDetail(
                userId = userId,
                role = role,
                secretId = secretId
            )
        )
    }
}
