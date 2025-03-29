package com.backend.sodam.domain.users.entity

import com.backend.sodam.domain.grades.entity.GradesEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "users_grade")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersGradeEntity(
    @Id
    @Column(name = "USER_GRADE_ID")
    val userGradeId: String,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "GRADE_ID")
    val grade: GradesEntity? = null,

    // 가변 필드
    startAt: LocalDateTime,
    endAt: LocalDateTime,
    validYN: Int,
): MutableBaseEntity() {

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