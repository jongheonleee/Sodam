package com.backend.sodam.domain.secrets.controller

import com.backend.sodam.domain.secrets.controller.request.SecretCreateRequest
import com.backend.sodam.domain.secrets.controller.request.SecretSearchRequest
import com.backend.sodam.domain.secrets.service.SecretService
import com.backend.sodam.domain.secrets.service.response.SecretCreateResponse
import com.backend.sodam.domain.secrets.service.response.SecretDetailResponse
import com.backend.sodam.domain.secrets.service.response.SecretSummaryResponse
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class SecretController(
    private val secretService: SecretService,
    private val tokenProvider: JwtTokenProvider
) {

    // 추후에 개발할 예정
    @PostMapping("/api/v1/secrets")
    fun createSecret(
        @Valid @RequestBody
        request: SecretCreateRequest
    ): SodamApiResponse<SecretCreateResponse> {
        return SodamApiResponse.ok(
            secretService.create(request.toCommand())
        )
    }

    @GetMapping("/api/v1/secrets")
    fun getSecrets(
        pageable: Pageable,
        secretSearchRequest: SecretSearchRequest
    ): SodamApiResponse<Page<SecretSummaryResponse>> {
        val command = secretSearchRequest.toCommand()
        return SodamApiResponse.ok(
            secretService.fetchFromClient(pageable, command)
        )
    }

    @GetMapping("/api/v1/secrets/{secretId}")
    @PreAuthorize("hasAnyRole('ROLE_BRONZE', 'ROLE_SILVER', 'ROLE_GOLD', 'ROLE_PLATINUM')")
    fun getSecretDetail(
        @PathVariable("secretId") secretId: Long
    ): SodamApiResponse<SecretDetailResponse> {
        return SodamApiResponse.ok(
            secretService.getSecretDetail(secretId)
        )
    }
}
