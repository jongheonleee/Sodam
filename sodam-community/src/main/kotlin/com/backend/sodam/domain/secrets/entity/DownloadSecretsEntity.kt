package com.backend.sodam.domain.secrets.entity

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

@Entity
@Table(name = "download_secrets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class DownloadSecretsEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "USER_DOWN_ID")
    val userDownId: String,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SECRETE_ID")
    val secret: SecretsEntity? = null

    // 가변 필드
) : MutableBaseEntity()
