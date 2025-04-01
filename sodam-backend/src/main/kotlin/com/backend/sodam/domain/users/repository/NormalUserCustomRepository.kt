package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface NormalUserCustomRepository {
    fun findByEmailWithRole(email: String): Optional<SodamUser>
    fun findSodamUserByUserId(userId: String): Optional<SodamUser>
    fun findProfileInfoForUser(userId: String): Optional<SodamUserDetail>
    fun findUserOwnLikeArticlesByPageBy(userId: String, pageable: Pageable): Page<SodamArticle>
    fun findUserOwnArticlesByPageBy(userId: String, pageable: Pageable): Page<SodamArticle>

    // 이거 옮길거임
    fun findProfileInfoForSocialUser(socialUserId: String): Optional<SodamUserDetail>
    fun findSocialUserOwnArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle>
    fun findSocialUserOwnLikeArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle>
}
