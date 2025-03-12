package com.backend.sodam.domain.users.controller

import com.backend.sodam.domain.users.controller.dto.LoginRequest
import com.backend.sodam.domain.users.controller.dto.SignupRequest
import com.backend.sodam.domain.users.controller.dto.toDto
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.dto.SignupResponse
import com.backend.sodam.global.commons.SodamApiResponse
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class AuthController(
    private val userService: UserService,
) {

    @PostMapping("/api/v1/auth/signup")
    fun signup(
        @RequestBody @Valid signupRequest : SignupRequest,
    ) : SodamApiResponse<SignupResponse> {
        return SodamApiResponse.ok(userService.signupUser(signupRequest.toDto()))
    }

    @PostMapping("/api/v1/auth/login")
    fun login(
        @RequestBody loginRequest : LoginRequest,
    ) : SodamApiResponse<String> {
        println(loginRequest)
        return SodamApiResponse.ok("dede")
    }
}