package com.backend.sodam.domain.tokens.service

import com.backend.sodam.domain.tokens.exception.TokenException
import com.backend.sodam.domain.tokens.repository.TokenRepository
import com.backend.sodam.domain.tokens.service.response.TokenResponse
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserRepository
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.response.UserResponse
import com.backend.sodam.global.port.KakaoTokenPort
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

@Service
@RequiredArgsConstructor
class TokenService(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val kakaoTokenPort : KakaoTokenPort,
    private val userService: UserService,

    @Value("\${jwt.secret}")
    val secretKey: String,
) {

    // accesstoken 기반으로 회원 정보를 조회함
    // - DB, redis 활용
    fun findUserByAccessToken(token: String): UserResponse {
        val claims = parseClaims(token)
        val userId = claims["userId"] ?: throw TokenException.InvalidTokenException() // 여기서 사용하는 userId는 email을 의미함
        println("userId: $userId")

        val foundUserByProviderId = userService.findByProviderId(userId.toString())
        if (ObjectUtils.isEmpty(foundUserByProviderId)) {
            throw UserException.UserNotFoundException()
        }

        return foundUserByProviderId!!

    // 소셜 회원인지 그냥 회원인지 구분해서 달리 처리해야함
    }

    // 카카오로부터 토큰을 받을 수 있음
    // 코드값을 전달받으면 해당 코드값을 카카오로 전달하여 토큰값을 받아옴
    fun getTokenFromKakao(code: String): String {
        return kakaoTokenPort.getAccessTokenByCode(code)
    }

    // 해당 토큰이 유효한지 판별 - 별도에 에러가 발생하지 않으면 성공
    fun validateToken(accessToken: String): Boolean = try {
        Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(accessToken)
        true
    } catch (e: JwtException) {
        false
    }


    // 새로운 토큰 생성
    fun createNewTokenForUser(email: String): TokenResponse {
        // 회원 아이디 기반으로 토큰 발급
        val accessToken = getToken(email, Duration.ofHours(5))
        val refreshToken = getToken(email, Duration.ofHours(24))

        // 발급된 토큰을 생성함
        return tokenRepository.createTokenForUser(email, accessToken, refreshToken)
    }

    fun createNewTokenForSocialUser(userId: String) : TokenResponse {
        // 회원 아이디 기반으로 토큰 발급
        val accessToken = getToken(userId, Duration.ofHours(5))
        val refreshToken = getToken(userId, Duration.ofHours(24))

        // 발급된 토큰을 생성함
        return tokenRepository.createTokenForSocialUser(userId, accessToken, refreshToken)
    }

    fun upsertTokenForSocialUser(providerId: String) : TokenResponse {
        val foundTokenBySocialUserId = tokenRepository.findTokenBySocialUserId(providerId)
        return when {
            foundTokenBySocialUserId.isPresent -> updateTokenForSocialUser(providerId)
            else -> createNewTokenForSocialUser(providerId)
        }
    }

    fun updateTokenForSocialUser(providerId: String): TokenResponse {
        // 토큰 생성
        val accessToken = getToken(providerId, Duration.ofHours(5))
        val refreshToken = getToken(providerId, Duration.ofHours(24))
        tokenRepository.updateTokenForSocialUser(providerId, accessToken, refreshToken)
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun upsertTokenForUser(email: String) : TokenResponse {
        val foundTokenByUserId = tokenRepository.findTokenByUserId(email)
        return when {
            foundTokenByUserId.isPresent -> updateTokenForUser(email)
            else -> createNewTokenForUser(email)
        }
    }

    fun updateTokenForUser(email: String) : TokenResponse {
        val accessToken = getToken(email, Duration.ofHours(5))
        val refreshToken = getToken(email, Duration.ofHours(24))
        tokenRepository.updateTokenForUser(email, accessToken, refreshToken)
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    private fun getToken(userId: String, expiredAt: Duration): String {
        val now = Date()
        val instant = now.toInstant()
        return Jwts.builder()
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(Date.from(instant.plus(expiredAt)))
            .signWith(getSignKey())
            .compact()
    }

    private fun getSignKey() : SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .body
        } catch (e : ExpiredJwtException) {
            e.claims
        }
    }
}
