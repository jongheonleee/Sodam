package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.users.repository.UserHistoryRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserHistoryService(
    private val userHistoryRepository: UserHistoryRepository
) {

    fun log(
        userId: String,
        userRole: String,
        clientIp: String,
        reqMethod: String,
        reqUrl: String,
        reqHeader: String,
        reqPayload: String
    ) {
        userHistoryRepository.createUserHistory(userId, userRole, clientIp, reqMethod, reqUrl, reqHeader, reqPayload)
    }
}
