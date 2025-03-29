package com.backend.sodam.domain.users.controller

import com.backend.sodam.domain.tokens.service.TokenService
import com.backend.sodam.domain.tokens.controller.response.TokenResponse
import com.backend.sodam.domain.users.controller.request.LoginRequest
import com.backend.sodam.domain.users.controller.request.SignupRequest
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.controller.response.UserSignupResponse
import com.backend.sodam.global.commons.ErrorCode
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.security.SodamAuthUser
import io.micrometer.common.util.StringUtils
import jakarta.servlet.http.HttpServletRequest
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
    private val tokenService: TokenService
) {

    // 회원가입
    @PostMapping("/api/v1/auth/signup")
    fun signup(
        @RequestBody @Valid
        signupRequest: SignupRequest
    ): SodamApiResponse<UserSignupResponse> {
        val command = signupRequest.toCommand()
        return SodamApiResponse.ok(userService.signupUser(command))
    }

    // 로그인
    @PostMapping("/api/v1/auth/login")
    fun login(
        @RequestBody @Valid
        loginRequest: LoginRequest
    ): SodamApiResponse<TokenResponse> {
        val token = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        val authenticate = authenticationManagerBuilder.`object`.authenticate(token)
        val principal = authenticate.principal as SodamAuthUser

        return SodamApiResponse.ok(tokenService.upsertTokenForUser(principal.email))
    }

    // oauth2 로그인
    @PostMapping("/api/v1/auth/callback")
    fun oauth2Callback(
        @RequestBody request: Map<String, String>
    ): SodamApiResponse<TokenResponse> {
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

        return SodamApiResponse.ok(
            tokenService.upsertTokenForSocialUser(foundKakaoUser.providerId)
        )
    }

    @PostMapping("/api/v1/reissue")
    fun reissueToken(
        httpServletRequest: HttpServletRequest
    ): SodamApiResponse<out TokenResponse> {
        val refreshToken = httpServletRequest.getHeader("refresh_token")
        val accessToken = httpServletRequest.getHeader("token")

        if (StringUtils.isBlank(refreshToken) || StringUtils.isBlank(accessToken)) {
            return SodamApiResponse.fail(ErrorCode.DEFAULT_ERROR, "토큰이 없습니다.")
        }
        return SodamApiResponse.ok(
            tokenService.reissueToken(accessToken, refreshToken)
        )
    }
}
