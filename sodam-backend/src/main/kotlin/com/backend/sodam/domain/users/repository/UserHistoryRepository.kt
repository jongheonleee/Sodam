package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.UsersHistoryEntity
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class UserHistoryRepository(
    private val userHistoryJpaRepository: UserHistoryJpaRepository,
) {

    @Transactional
    fun createUserHistory(
        userId: String, userRole: String, clientIp : String,
        reqMethod: String, reqUrl: String, reqHeader: String,
        reqPayload : String
    ) {
        userHistoryJpaRepository.save(
            UsersHistoryEntity(
                userId = userId,
                userRole = userRole,
                clientIp = clientIp,
                reqMethod = reqMethod,
                reqUrl = reqUrl,
                reqHeader = reqHeader,
                reqBody = reqPayload,
            )
        )
    }
}