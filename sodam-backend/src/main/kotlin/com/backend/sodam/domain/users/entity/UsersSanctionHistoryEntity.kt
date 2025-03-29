package com.backend.sodam.domain.users.entity

import com.backend.sodam.domain.policy.entity.SanctionsPolicyEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "users_sanction_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersSanctionHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SANCTION_ID")
    val sanctionsHistoryId: Long,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    var user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    var socialUser: SocialUsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "POLICY_ID")
    var sanctionPolicy: SanctionsPolicyEntity? = null,

    // 가변 필드
    startAt: LocalDateTime,
    endAt: LocalDateTime,
): MutableBaseEntity() {

    @Column(name = "START_AT")
    var startAt: LocalDateTime? = null
        protected set

    @Column(name = "END_AT")
    var endAt: LocalDateTime? = null
        protected set
}