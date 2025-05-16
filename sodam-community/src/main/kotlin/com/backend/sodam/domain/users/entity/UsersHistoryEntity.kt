package com.backend.sodam.domain.users.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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
    val userHistoryId: Long? = null,

    @Column(name = "USER_ID")
    val userId: String,

    // 가변 필드
    userRole: String,
    clientIp: String,
    reqMethod: String,
    reqUrl: String,
    reqHeader: String,
    reqBody: String
) : MutableBaseEntity() {

    @Column(name = "USER_ROLE")
    var userRole = userRole
        protected set

    @Column(name = "REQ_IP")
    var clientIp = clientIp
        protected set

    @Column(name = "REQ_METHOD")
    var reqMethod = reqMethod
        protected set

    @Column(name = "REQ_URL")
    var reqUrl = reqUrl
        protected set

    @Column(name = "REQ_HEADER", columnDefinition = "TEXT")
    var reqHeader = reqHeader
        protected set

    @Column(name = "REQ_BODY", columnDefinition = "TEXT")
    var reqBody = reqBody
        protected set
}
