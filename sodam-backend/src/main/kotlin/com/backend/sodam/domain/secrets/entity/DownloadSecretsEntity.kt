package com.backend.sodam.domain.secrets.entity

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

    // FK(추후에 연관관계 처리)
    // - 회원 아이디 : 다운로드 - 회원  = N : 1
    // - 시크릿 아이디 : 다운로드 - 시크릿 = N : 1
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity,

    @ManyToOne
    @JoinColumn(name = "SECRETE_ID")
    val secret: SecretsEntity

    // 가변 필드
) : MutableBaseEntity()
