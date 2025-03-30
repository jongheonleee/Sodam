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
@Table(name = "view_secrets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ViewSecretsEntity(
    @Id
    @Column(name = "USER_VIEW_ID")
    val userViewId: String,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SECRETE_ID")
    val secret: SecretsEntity? = null,

): MutableBaseEntity()