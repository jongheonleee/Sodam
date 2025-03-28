package com.backend.sodam.domain.users.entity

import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import lombok.AccessLevel
import lombok.NoArgsConstructor
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "users_position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersPositionsEntity(
    @Id
    @Column(name = "USER_POSITION_ID")
    val userPositionId: String,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "POSITION_ID")
    val position: PositionsEntity? = null,

    ): MutableBaseEntity() {
}