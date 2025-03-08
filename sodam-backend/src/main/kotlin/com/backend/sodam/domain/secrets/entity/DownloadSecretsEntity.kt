package com.backend.sodam.domain.secrets.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.UUID

@Entity
@Table(name = "download_secrets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class DownloadSecretsEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "USER_DOWN_ID")
    val userDownId: UUID

    // FK(추후에 연관관계 처리)
    // - 회원 아이디 : 다운로드 - 회원  = N : 1
    // - 시크릿 아이디 : 다운로드 - 시크릿 = N : 1

    // 가변 필드
) : MutableBaseEntity()
