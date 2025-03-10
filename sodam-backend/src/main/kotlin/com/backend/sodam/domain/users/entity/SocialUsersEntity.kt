package com.backend.sodam.domain.users.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.UUID

@Entity
@Table(name = "social_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SocialUsersEntity(
    // PK 및 불변 필드
    @Id
    @Column(name = "SOCIAL_USER_ID")
    val socialUserId: String,

    @Column(name = "PROVIDER")
    val provider: String,

    @Column(name = "PROVIDER_ID")
    val providerId: String,

    // FK(추후에 연관관계 매핑)

    // 가변 필드
    userName: String
) : MutableBaseEntity() {

    @Column(name = "USER_NAME")
    var userName = userName
        protected set
}
