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

@Entity
@Table(name = "users_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersHistoryEntity(
    // pk 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_HISTORY_ID")
    val userHistoryId: Long,

    // FK(추후에 연관관계 처리)
    // - 회원 아이디 : 회원 히스토리 - 회원 : N : 1
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity,

    // 가변 필드
    userRole: String,
    reqIp: String,
    reqMethod: String,
    reqUrl: String,
    reqHeader: String,
    reqBody: String
) : MutableBaseEntity() {

    @Column(name = "USER_ROLE")
    var userRole = userRole
        protected set

    @Column(name = "REQ_IP")
    var reqIp = reqIp
        protected set

    @Column(name = "REQ_METHOD")
    var reqMethod = reqMethod
        protected set

    @Column(name = "REQ_URL")
    var reqUrl = reqUrl
        protected set

    @Column(name = "REQ_HEADER")
    var reqHeader = reqHeader
        protected set

    @Column(name = "REQ_BODY")
    var reqBody = reqBody
        protected set
}
