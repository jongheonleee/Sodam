package com.backend.sodam.domain.users.controller

import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.response.UserResponse
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class UserController(
    private val userService: UserService,
    private val tokenProvider: JwtTokenProvider,
) {

    @GetMapping("/api/v1/users/info")
    fun getUserInfo(): SodamApiResponse<UserResponse> { // 반환타입 다시 책정하기
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            userService.findByUserId(userId)
        )
    }
}