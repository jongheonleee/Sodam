package com.backend.sodam.domain.users.controller

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.users.controller.request.UserUpdateRequest
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.controller.response.UserProfileResponse
import com.backend.sodam.domain.users.controller.response.UserUpdateResponse
import com.backend.sodam.domain.users.service.usescase.DeleteUserUseCase
import com.backend.sodam.domain.users.service.usescase.FetchUserUseCase
import com.backend.sodam.domain.users.service.usescase.RegisterUserUseCase
import com.backend.sodam.domain.users.service.usescase.UpdateUserUseCase
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class UserController(
    // 회원 관련 빈 DI
    private val fetchUserUseCase: FetchUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,

    private val userService: UserService, // 해당 부분 위같은 형태로 쪼개기
    private val tokenProvider: JwtTokenProvider
) {

    @GetMapping("/api/v1/users/info")
    fun getUserInfo(): SodamApiResponse<UserProfileResponse> { // 반환타입 다시 책정하기
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            userService.findUserProfileInfo(userId)
        )
    }

    // 자신이 작성한 게시글 목록 조회
    @GetMapping("/api/v1/users/articles")
    fun getUserArticles(
        pageable: Pageable
    ): SodamApiResponse<Page<ArticleSummaryResponse>> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            userService.getOwnArticles(userId = userId, pageable = pageable)
        )
    }

    @GetMapping("/api/v1/users/like/articles")
    fun getUserLikedArticles(
        pageable: Pageable
    ): SodamApiResponse<Page<ArticleSummaryResponse>> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            userService.getOwnLikeArticles(userId = userId, pageable = pageable)
        )
    }

    // 회원 정보 수정 처리
    @PutMapping("/api/v1/users/info")
    fun updateUserInfo(
        @RequestBody @Valid userUpdateRequest: UserUpdateRequest,
    ): SodamApiResponse<UserUpdateResponse> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            updateUserUseCase.updateUserInfo(userId = userId, userUpdateCommand = userUpdateRequest.toCommand())
        )
    }
}
