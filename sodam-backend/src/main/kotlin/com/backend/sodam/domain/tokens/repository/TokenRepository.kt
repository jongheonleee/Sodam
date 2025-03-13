package com.backend.sodam.domain.tokens.repository

import com.backend.sodam.domain.tokens.entity.UsersTokenEntity
import com.backend.sodam.domain.tokens.service.dto.TokenResponse
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class TokenRepository(
    private val userJpaRepository: UserJpaRepository,
    private val tokenJpaRepository: TokenJpaRepository
) {

    // 유저 조회
    // 해당 유저와 전달받은 토큰값을 기반으로 토큰 엔티티 생성
    // 생성된 토큰 엔티티를 저장함
    // 토큰 정보를 반환함
    @Transactional
    fun create(userId: String, accessToken: String, refreshToken: String): TokenResponse {
        val foundUserByUserId = userJpaRepository.findByUserId(userId)
            .orElseThrow { UserException.UserNotFoundException() }

        val tokenEntity = UsersTokenEntity.newTokenEntity(
            foundUserByUserId,
            accessToken,
            refreshToken
        )

        tokenJpaRepository.save(tokenEntity)

        return TokenResponse(
            accessToken,
            refreshToken
        )
    }

    @Transactional
    fun findTokenByUserId(userId: String): TokenResponse {
        return tokenJpaRepository.findByUserId(userId)
            .map { TokenResponse(it.accessToken, it.refreshToken) }
            .orElseThrow { RuntimeException() } // 추후에 예외 추가
    }

    @Transactional
    fun updateToken(userId: String, accessToken: String, refreshToken: String) {
        val foundTokenByUserId = tokenJpaRepository.findByUserId(userId).orElseThrow { throw RuntimeException() } // 추후에 예외 정의하기
        foundTokenByUserId.updateToken(accessToken, refreshToken)
        tokenJpaRepository.save(foundTokenByUserId)
    }
}
