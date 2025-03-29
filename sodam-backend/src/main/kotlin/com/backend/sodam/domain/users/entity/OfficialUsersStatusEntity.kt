package com.backend.sodam.domain.users.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "official_users_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class OfficialUsersStatusEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OFFICIAL_ID")
    val officialId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null,

    // 가변 필드
    startAt: LocalDateTime,
    endAt: LocalDateTime,
    validYN: Int
) : MutableBaseEntity() {

    @Column(name = "START_AT")
    var startAt: LocalDateTime = startAt
        protected set

    @Column(name = "END_AT")
    var endAt: LocalDateTime = endAt
        protected set

    @Column(name = "VALID_YN")
    var validYN: Int = validYN
        protected set
}
