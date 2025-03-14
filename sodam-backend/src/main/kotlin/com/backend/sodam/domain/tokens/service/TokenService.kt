package com.backend.sodam.domain.tokens.service

import com.backend.sodam.domain.tokens.exception.TokenException
import com.backend.sodam.domain.tokens.repository.TokenRepository
import com.backend.sodam.domain.tokens.service.dto.TokenResponse
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserRepository
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
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

@Service
@RequiredArgsConstructor
class TokenService(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository,
    private val kakaoTokenPort : KakaoTokenPort,

    @Value("\${jwt.secret}")
    val secretKey: String,
) {

    // accesstoken 기반으로 회원 정보를 조회함
    // - DB, redis 활용
    fun findUserByAccessToken(token: String): UserResponse {
        val claims = parseClaims(token)
        val userId = claims["userId"] ?: throw TokenException.UserIdNotFoundOnTokenException()

        return userRepository.findByProviderId(userId.toString())
            .map { UserResponse.toUserResponse(it) }
            .orElseThrow { UserException.UserNotFoundException() }
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

    fun upsertTokenForSocialUser(providerId: String) : String {
        val foundTokenBySocialUserId = tokenRepository.findTokenBySocialUserId(providerId)

        // 토큰 생성
        val accessToken = getToken(providerId, Duration.ofHours(5))
        val refreshToken = getToken(providerId, Duration.ofHours(24))


        when {
            foundTokenBySocialUserId.isEmpty -> {
                // 토큰 등록
                tokenRepository.createTokenForSocialUser(providerId, accessToken, refreshToken)
            }
            foundTokenBySocialUserId.isPresent -> {
                // 토큰 업데이트
                tokenRepository.updateToken(providerId, accessToken, refreshToken)

            }
        }

        return accessToken
    }

    fun upsertTokenForUser(email: String) : String {
        val foundTokenByUserId = tokenRepository.findTokenByUserId(email)

        // 토큰 생성
        val accessToken = getToken(email, Duration.ofHours(5))
        val refreshToken = getToken(email, Duration.ofHours(24))

        when {
            foundTokenByUserId.isEmpty -> {
                // 토큰 등록
                tokenRepository.createTokenForUser(email, accessToken, refreshToken)
            }
            foundTokenByUserId.isPresent -> {
                // 토큰 업데이트
                tokenRepository.updateToken(email, accessToken, refreshToken)

            }
        }

        return accessToken
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
