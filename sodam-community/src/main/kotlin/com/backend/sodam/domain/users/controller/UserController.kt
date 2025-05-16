package com.backend.sodam.domain.users.controller

import com.backend.sodam.domain.articles.service.response.ArticleSummaryResponse
import com.backend.sodam.domain.users.controller.request.UserUpdateRequest
import com.backend.sodam.domain.users.service.response.UserProfileResponse
import com.backend.sodam.domain.users.service.response.UserUpdateResponse
import com.backend.sodam.domain.users.service.usescase.FetchUserUseCase
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
    private val updateUserUseCase: UpdateUserUseCase,

    // 토큰 관련 빈 DI
    private val tokenProvider: JwtTokenProvider
) {

    @GetMapping("/api/v1/users/info")
    fun getUserInfo(): SodamApiResponse<UserProfileResponse> { // 반환타입 다시 책정하기
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(fetchUserUseCase.findUserProfileInfo(userId))
    }

    // 자신이 작성한 게시글 목록 조회
    @GetMapping("/api/v1/users/articles")
    fun getUserArticles(
        pageable: Pageable
    ): SodamApiResponse<Page<ArticleSummaryResponse>> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(fetchUserUseCase.getOwnArticles(userId = userId, pageable = pageable))
    }

    @GetMapping("/api/v1/users/like/articles")
    fun getUserLikedArticles(
        pageable: Pageable
    ): SodamApiResponse<Page<ArticleSummaryResponse>> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(fetchUserUseCase.getOwnLikeArticles(userId = userId, pageable = pageable))
    }

    // 회원 정보 수정 처리
    @PutMapping("/api/v1/users/info")
    fun updateUserInfo(
        @RequestBody @Valid
        userUpdateRequest: UserUpdateRequest
    ): SodamApiResponse<UserUpdateResponse> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(updateUserUseCase.updateUserInfo(userId = userId, userUpdateCommand = userUpdateRequest.toCommand()))
    }
}
