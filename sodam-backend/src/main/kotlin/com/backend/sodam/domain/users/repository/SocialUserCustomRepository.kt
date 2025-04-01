package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface SocialUserCustomRepository {
    fun findByProviderId(providerId: String): Optional<SocialUsersEntity>
    fun findSodamUserByUserId(userId: String): Optional<SodamUser>
    fun findProfileInfoForSocialUser(socialUserId: String): Optional<SodamUserDetail>
    fun findSocialUserOwnArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle>
    fun findSocialUserOwnLikeArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle>
}
