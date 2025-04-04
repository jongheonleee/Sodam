package com.backend.sodam.domain.users.service.usescase

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.users.controller.response.SocialUserResponse
import com.backend.sodam.domain.users.controller.response.UserProfileResponse
import com.backend.sodam.domain.users.controller.response.UserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FetchUserUseCase {
    fun findByEmail(email: String): UserResponse
    fun findByUserId(userId: String): UserResponse
    fun findKakaoUser(accessToken: String): SocialUserResponse
    fun findByProviderId(providerId: String): UserResponse?
    fun findUserProfileInfo(userId: String): UserProfileResponse
    fun getOwnArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse>
    fun getOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse>
}