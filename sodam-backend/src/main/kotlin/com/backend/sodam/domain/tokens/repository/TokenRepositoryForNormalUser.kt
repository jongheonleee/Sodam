package com.backend.sodam.domain.tokens.repository

import com.backend.sodam.domain.tokens.service.response.TokenResponse
import com.backend.sodam.domain.tokens.entity.UsersTokenEntity
import com.backend.sodam.domain.tokens.exception.TokenException
import com.backend.sodam.domain.tokens.service.port.CreateTokenPort
import com.backend.sodam.domain.tokens.service.port.FetchTokenPort
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class TokenRepositoryForNormalUser(
    private val tokenJpaRepository: TokenJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository
): CreateTokenPort, FetchTokenPort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

    @Transactional
    override fun createToken(userId: String, accessToken: String, refreshToken: String): TokenResponse {
        val userEntity = normalUserJpaRepository.findByUserEmail(email = userId).get()
        val tokenEntity = UsersTokenEntity.newTokenEntity(
            userEntity = userEntity,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
        tokenJpaRepository.save(tokenEntity)
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    @Transactional(readOnly = true)
    override fun findTokenByUserId(userId: String): Optional<TokenResponse> =
        tokenJpaRepository.findByUserId(userId = userId)
                          .map { TokenResponse(it.accessToken, it.refreshToken) }

    // 소셜 유저 조회
    // 해당 유저와 전달받은 토큰값을 기반으로 토큰 엔티티 생성
    // 생성된 토큰 엔티티를 저장함
    // 토큰 정보를 반환함
    @Transactional
    fun createTokenForSocialUser(userId: String, accessToken: String, refreshToken: String): TokenResponse {
        val foundSocialUserByProviderId = socialUserJpaRepository.findByProviderId(userId) // socialUserId != userId, userId == providerId
            .orElseThrow { UserException.UserNotFoundException() }

        val tokenEntity = UsersTokenEntity.newTokenEntity(
            foundSocialUserByProviderId,
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
    fun createTokenForUser(email: String, accessToken: String, refreshToken: String): TokenResponse {
        val foundUserByEmail = normalUserJpaRepository.findByUserEmail(email)
            .orElseThrow { UserException.UserNotFoundException() }

        val tokenEntity = UsersTokenEntity.newTokenEntity(
            foundUserByEmail,
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
    fun findTokenBySocialUserId(socialUserId: String): Optional<TokenResponse> {
        return tokenJpaRepository.findBySocialUserId(socialUserId)
            .map { TokenResponse(it.accessToken, it.refreshToken) }
    }


    @Transactional
    fun updateTokenForUser(email: String, accessToken: String, refreshToken: String) {
        val foundTokenByEmail = tokenJpaRepository.findByUserId(email)
            .orElseThrow { TokenException.UserTokenNotFoundException() }
        foundTokenByEmail.updateToken(accessToken, refreshToken)
        tokenJpaRepository.save(foundTokenByEmail)
    }

    // providerId => socialUserId
    @Transactional
    fun updateTokenForSocialUser(providerId: String, accessToken: String, refreshToken: String) {
        // 추후 리팩토링 대상
        val foundSocialUserByProviderId = socialUserJpaRepository.findByProviderIdWithSubscription(providerId)
        if (foundSocialUserByProviderId.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundTokenByProviderId = tokenJpaRepository.findBySocialUserId(foundSocialUserByProviderId.get().userId)
            .orElseThrow { TokenException.UserTokenNotFoundException() } // 추후에 예외 정의하기

        foundTokenByProviderId.updateToken(accessToken, refreshToken)
        tokenJpaRepository.save(foundTokenByProviderId)
    }
}
