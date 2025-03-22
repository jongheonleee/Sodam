package com.backend.sodam.domain.users.entity

import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "USER_ID")
    val userId: String,

    // 가변필드
    userEmail: String,
    userName: String,
    introduce: String,
    profileImageUrl: String,
    password: String
) : MutableBaseEntity() {

    @Column(name = "USER_EMAIL")
    var userEmail = userEmail
        protected set

    @Column(name = "USER_NAME")
    var userName = userName
        protected set

    @Column(name = "USER_INTRODUCE")
    var userIntroduce = introduce
        protected set

    @Column(name = "USER_IMAGE")
    var userImage = profileImageUrl
        protected set

    @Column(name = "PASSWORD")
    var password = password
        protected set

    fun toDomain(): SodamUser {
        return SodamUser(
            userId = this.userId,
            username = this.userName,
            encryptedPassword = this.password,
            email = this.userEmail,
            introduce = this.userIntroduce,
            profileImageUrl = this.userImage
        )
    }
}
