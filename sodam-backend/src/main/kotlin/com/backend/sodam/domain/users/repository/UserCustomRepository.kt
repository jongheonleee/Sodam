package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.users.model.SodamUserDetail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface UserCustomRepository {
    fun findProfileInfoForSocialUser(socialUserId: String): Optional<SodamUserDetail>
    fun findProfileInfoForUser(userId: String): Optional<SodamUserDetail>
    fun findUserOwnArticlesByPageBy(userId: String, pageable: Pageable): Page<SodamArticle>
    fun findSocialUserOwnArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle>
}
