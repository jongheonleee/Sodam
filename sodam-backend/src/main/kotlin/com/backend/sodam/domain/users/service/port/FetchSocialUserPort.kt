package com.backend.sodam.domain.users.service.port

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import java.util.*

// 소셜회원의 경우, 일반회원의 fetch 기능 이외에 추가적으로 지원해야 하는 기능이 있음
interface FetchSocialUserPort: FetchUserPort {
    // 이 부분 추후에 도메인으로 반환하게끔 구성하기
    fun findEntityByProviderId(providerId: String): Optional<SocialUsersEntity>
    fun findByProviderId(providerId: String): Optional<SodamUser>
    fun isExistsByProviderId(providerId: String): Boolean
}