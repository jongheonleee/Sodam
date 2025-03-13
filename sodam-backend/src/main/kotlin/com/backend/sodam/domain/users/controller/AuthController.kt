package com.backend.sodam.domain.users.controller

import com.backend.sodam.domain.users.controller.dto.LoginRequest
import com.backend.sodam.domain.users.controller.dto.SignupRequest
import com.backend.sodam.domain.users.controller.dto.toDto
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.dto.SignupResponse
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.security.SodamAuthUser
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class AuthController(
    private val userService: UserService,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
) {

    @PostMapping("/api/v1/auth/signup")
    fun signup(
        @RequestBody @Valid signupRequest : SignupRequest,
    ) : SodamApiResponse<SignupResponse> {
        return SodamApiResponse.ok(userService.signupUser(signupRequest.toDto()))
    }

    // Authentication 토큰생성
    // SecurityContext에서 인증 정보 조회
    // 토큰값 조회 및 반환 -> 프론트엔드에서 토큰 활용
    // 추후에 토큰 발급해야함
    @PostMapping("/api/v1/auth/login")
    fun login(
        @RequestBody @Valid loginRequest : LoginRequest,
    ) : SodamApiResponse<String> {
        val token = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        val authenticate = authenticationManagerBuilder.`object`.authenticate(token)
        val principal = authenticate.principal as SodamAuthUser
        return SodamApiResponse.ok("access-token")
    }

    // ouath2 로그인 처리(kakao)
    @PostMapping("/api/v1/auth/callback")
    fun oauthCallback(
        @RequestBody request : Map<String, String>
    ) : SodamApiResponse<String> {
        val code = request.get("code")
        println("code: $code")
        return SodamApiResponse.ok("access-token")
    }
}