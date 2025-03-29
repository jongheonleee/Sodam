package com.backend.sodam.domain.secrets.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

// 구독자 전용 게시글 검증 이력
// 검증대기, 검증처리중, 검증완료, ...
@Entity
@Table(name = "secret_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SecretsHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    val secretHistoryId: Long? = null,

    @OneToOne
    @JoinColumn(name = "SECRETE_ID")
    var secret: SecretsEntity? = null,

    // 추후에 Enum으로 구성
    validStep: Int
): MutableBaseEntity() {

    @Column(name = "VALID_STEP")
    var validStep: Int = validStep
        protected set


}