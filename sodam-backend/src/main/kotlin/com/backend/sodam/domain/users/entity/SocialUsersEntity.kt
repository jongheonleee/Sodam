package com.backend.sodam.domain.users.entity

import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.*

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

    userName: String
) : MutableBaseEntity() {

    @Column(name = "USER_NAME")
    var userName = userName
        protected set

    fun toDomain(): SodamUser {
        return SodamUser(
            userId = this.socialUserId,
            username = this.userName,
            provider = this.provider,
            providerId = this.providerId,
            userType = UserType.SOCIAL
        )
    }

    companion object {
        fun newEntity(provider: String, providerId: String, userName: String): SocialUsersEntity {
            return SocialUsersEntity(
                socialUserId = UUID.randomUUID().toString(),
                provider = provider,
                providerId = providerId,
                userName = userName
            )
        }
    }
}
