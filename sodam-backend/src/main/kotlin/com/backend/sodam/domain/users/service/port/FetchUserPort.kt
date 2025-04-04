package com.backend.sodam.domain.users.service.port

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.domain.users.model.UserType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface FetchUserPort {
    fun isTarget(userType: UserType): Boolean
    fun isExistsByUserId(userId: String): Boolean
    fun isExistsByEmail(email: String): Boolean
    fun findByEmail(email: String): SodamUser
    fun findByUserId(userId: String): Optional<SodamUser>
    fun findProfileInfo(userId: String): Optional<SodamUserDetail>
    fun findOwnArticlesByPageBy(pageable: Pageable, userId: String): Page<ArticleSummaryResponse>
    fun findOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse>
}
