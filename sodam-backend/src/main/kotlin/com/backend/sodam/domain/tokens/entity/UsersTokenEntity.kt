package com.backend.sodam.domain.tokens.entity

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "user_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersTokenEntity(

    // pk 및 불변 필드
    @Id
    @Column(name = "TOKEN_ID")
    val tokenId: String,

    // FK(추후에 연관관계 처리)
    // - 회원 아이디 : 회원 토큰 - 회원 : N : 1
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = true)
    val user: UsersEntity? = null,

    // - 소셜 회원 :회원 토큰 - 소셜 화원 : N : 1
    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID", nullable = true) // 이 부분 위험
    val socialUser: SocialUsersEntity? = null,

    // 가변 필드
    accessToken: String,
    refreshToken: String,
    accessTokenExpiresAt: LocalDateTime,
    refreshTokenExpiresAt: LocalDateTime

) : MutableBaseEntity() {

    @Column(name = "ACCESS_TOKEN")
    var accessToken = accessToken
        protected set

    @Column(name = "REFRESH_TOKEN")
    var refreshToken = refreshToken
        protected set

    @Column(name = "ACCESS_TOKEN_EXPIRES_AT")
    var accessTokenExpiresAt = accessTokenExpiresAt
        protected set

    @Column(name = "REFRESH_TOKEN_EXPIRES_AT")
    var refreshTokenExpiresAt = refreshTokenExpiresAt
        protected set

    fun updateToken(accessToken: String, refreshToken: String) {
        val now = LocalDateTime.now()
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.accessTokenExpiresAt = getAccessTokenExpireAt(now)
        this.refreshTokenExpiresAt = getRefreshTokenExpireAt(now)
    }

    companion object {
        fun getAccessTokenExpireAt(now: LocalDateTime): LocalDateTime {
            return now.plusHours(5) // 비즈니스 상 5시간으로 설정
        }

        fun getRefreshTokenExpireAt(now: LocalDateTime): LocalDateTime {
            return now.plusHours(24) // 비즈니스 상 24시간으로 설정
        }

        fun newTokenEntity(userEntity: UsersEntity, accessToken: String, refreshToken: String): UsersTokenEntity {
            val now = LocalDateTime.now()
            return UsersTokenEntity(
                tokenId = UUID.randomUUID().toString(),
                user = userEntity,
                accessToken = accessToken,
                refreshToken = refreshToken,
                accessTokenExpiresAt = getAccessTokenExpireAt(now),
                refreshTokenExpiresAt = getRefreshTokenExpireAt(now)
            )
        }

        fun newTokenEntity(socialUsersEntity: SocialUsersEntity, accessToken: String, refreshToken: String): UsersTokenEntity {
            val now = LocalDateTime.now()
            return UsersTokenEntity(
                tokenId = UUID.randomUUID().toString(),
                socialUser = socialUsersEntity,
                accessToken = accessToken,
                refreshToken = refreshToken,
                accessTokenExpiresAt = getAccessTokenExpireAt(now),
                refreshTokenExpiresAt = getRefreshTokenExpireAt(now)
            )
        }
    }
}
