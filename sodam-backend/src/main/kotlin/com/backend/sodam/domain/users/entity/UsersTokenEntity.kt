package com.backend.sodam.domain.users.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.UUID

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
    @JoinColumn(name = "USER_ID")
    val user : UsersEntity,

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
}
