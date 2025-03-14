package com.backend.sodam.domain.users.controller

import com.backend.sodam.domain.tokens.service.TokenService
import com.backend.sodam.domain.users.controller.request.LoginRequest
import com.backend.sodam.domain.users.controller.request.SignupRequest
import com.backend.sodam.domain.users.controller.request.toCommand
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.response.UserSignupResponse
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.security.SodamAuthUser
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.util.ObjectUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class AuthController(
    private val userService: UserService,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val tokenService: TokenService,
) {

    @PostMapping("/api/v1/auth/signup")
    fun signup(
        @RequestBody @Valid
        signupRequest: SignupRequest
    ): SodamApiResponse<UserSignupResponse> {
        val command = signupRequest.toCommand()
        return SodamApiResponse.ok(userService.signupUser(command))
    }

    // Authentication 토큰생성
    // SecurityContext에서 인증 정보 조회
    // 토큰값 조회 및 반환 -> 프론트엔드에서 토큰 활용
    // 추후에 토큰 발급해야함
    @PostMapping("/api/v1/auth/login")
    fun login(
        @RequestBody @Valid
        loginRequest: LoginRequest
    ): SodamApiResponse<String> {
        val token = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        val authenticate = authenticationManagerBuilder.`object`.authenticate(token)
        val principal = authenticate.principal as SodamAuthUser
        return SodamApiResponse.ok(tokenService.upsertTokenForUser(principal.email))
    }

    // ouath2 로그인 처리(kakao)
    // 코드를 통해 accesstoken을 조회한다.
    // 소셜 사용자가 이미 존재하는지 확인
    // 만약 존재하지 않으면 회원가입 처리
    // 토큰 발급해서 반환
    @PostMapping("/api/v1/auth/callback")
    fun oauth2Callback(
        @RequestBody request: Map<String, String>
    ): SodamApiResponse<String> {
        val code = request["code"] ?: throw RuntimeException()
        val accessTokenFromKakao = tokenService.getTokenFromKakao(code)
        val foundKakaoUser = userService.findKakaoUser(accessTokenFromKakao)
        val foundUserByProviderId = userService.findByProviderId(foundKakaoUser.providerId)
        if (ObjectUtils.isEmpty(foundUserByProviderId)) {
            userService.signupSocialUser(
                SocialUserSignupCommand(
                    username = foundKakaoUser.name,
                    provider = foundKakaoUser.provider,
                    providerId = foundKakaoUser.providerId
                )
            )
        }
        return SodamApiResponse.ok(tokenService.upsertTokenForSocialUser(foundKakaoUser.providerId))
    }
}
