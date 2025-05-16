package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SocialUserCustomRepository {
    fun findByProviderIdWithSubscription(providerId: String): Optional<SodamUser>
    fun findSodamUserByUserId(userId: String): Optional<SodamUser>
    fun findProfileInfoForSocialUser(socialUserId: String): Optional<SodamUserDetail>
    fun findSocialUserOwnArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle>
    fun findSocialUserOwnLikeArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle>
}
