package com.backend.sodam.domain.tokens.service

import com.backend.sodam.domain.tokens.controller.response.TokenResponse
import com.backend.sodam.domain.tokens.exception.TokenException
import com.backend.sodam.domain.tokens.repository.TokenRepositoryForNormalUser
import com.backend.sodam.domain.tokens.service.port.CreateTokenPort
import com.backend.sodam.domain.tokens.service.port.FetchTokenPort
import com.backend.sodam.domain.users.controller.response.UserResponse
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserRepository
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.port.FetchUserPort
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
    @Value("\${jwt.secret}")
    val secretKey: String,
    // 회원
    private val fetchUserPorts: List<FetchUserPort>,

    private val createTokenPorts: List<CreateTokenPort>,
    private val fetchTokenPorts: List<FetchTokenPort>,
    private val tokenRepositoryForNormalUser: TokenRepositoryForNormalUser,
    private val kakaoTokenPort: KakaoTokenPort,
    private val userService: UserService,
    private val normalUserRepository: NormalUserRepository,
) {

    // accesstoken 기반으로 회원 정보를 조회함
    // - DB, redis 활용
    fun findUserByAccessToken(token: String): UserResponse {
        val claims = parseClaims(token)
        val userId = claims["userId"] ?: throw TokenException.InvalidTokenException() // 여기서 사용하는 userId는 email을 의미함
        // 소셜 회원 먼저 조회, 없으면 일반 회원 조회. 그래도 없으면 예외 발생
        return userService.findByProviderId(userId.toString())
            ?: userService.findByEmail(userId.toString())
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
    fun createNewTokenForNormalUser(email: String): TokenResponse {
        val accessToken = getToken(email, Duration.ofHours(5))
        val refreshToken = getToken(email, Duration.ofHours(24))
        val createTokenPort = getCreateTokenPortByUserType(userType = UserType.NORMAL)
        return createTokenPort.createToken(userId = email, accessToken = accessToken, refreshToken = refreshToken)
    }

    fun createNewTokenForSocialUser(userId: String): TokenResponse {
        // 회원 아이디 기반으로 토큰 발급
        val accessToken = getToken(userId, Duration.ofHours(5))
        val refreshToken = getToken(userId, Duration.ofHours(24))
        val createTokenPort = getCreateTokenPortByUserType(userType = UserType.SOCIAL)
        return createTokenPort.createToken(userId = userId, accessToken = accessToken, refreshToken = refreshToken)
    }

    fun upsertTokenForSocialUser(providerId: String): TokenResponse {
        val fetchTokenPort = getFetchTokenPortByUserType(userType = UserType.SOCIAL)
        val foundTokenPort = fetchTokenPort.findTokenByUserId(userId = providerId)
        return when(foundTokenPort.isPresent) {
            true -> updateTokenForSocialUser(providerId)
            false -> createNewTokenForSocialUser(providerId)
        }
    }

    fun updateTokenForSocialUser(providerId: String): TokenResponse {
        // 토큰 생성
        val accessToken = getToken(providerId, Duration.ofHours(5))
        val refreshToken = getToken(providerId, Duration.ofHours(24))
        tokenRepositoryForNormalUser.updateTokenForSocialUser(providerId, accessToken, refreshToken)
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun upsertTokenForUser(email: String): TokenResponse {
        val fetchTokenPort = getFetchTokenPortByUserType(userType = UserType.NORMAL)
        val foundToken = fetchTokenPort.findTokenByUserId(email)
        return when(foundToken.isPresent) {
            true -> updateTokenForUser(email)
            false -> createNewTokenForNormalUser(email)
        }
    }

    fun updateTokenForUser(email: String): TokenResponse {
        val accessToken = getToken(email, Duration.ofHours(5))
        val refreshToken = getToken(email, Duration.ofHours(24))
        tokenRepositoryForNormalUser.updateTokenForUser(email, accessToken, refreshToken)
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    // 토큰 재발행 - 이 부분 로직 꼬인듯, 일반회원은 email, 소셜회원은 socialUserId
    fun reissueToken(accessToken: String, refreshToken: String): TokenResponse {
        val claimsJws = Jwts.parser()
                                           .setSigningKey(secretKey)
                                           .build()
                                           .parseClaimsJws(accessToken)
        val userId = claimsJws.payload["userId"] as String
        val sodamUserOptional = normalUserRepository.findByUserId(userId)
        if (sodamUserOptional.isEmpty) {
            throw TokenException.InvalidTokenException()
        }


        val sodamUser = sodamUserOptional.get()
        when (sodamUser.userType) {
            UserType.SOCIAL -> {
                val foundTokenBySocialUserId = tokenRepositoryForNormalUser.findTokenBySocialUserId(
                    socialUserId = sodamUser.userId
                )

                val sodamToken = foundTokenBySocialUserId.get()
                if (sodamToken.refreshToken != refreshToken) {
                    throw TokenException.InvalidTokenException()
                }

                return updateTokenForSocialUser(userId)
            }
            else -> {
                val foundTokenByUserId = tokenRepositoryForNormalUser.findTokenByUserId(
                    userId = sodamUser.userId
                )
                val sodamToken = foundTokenByUserId.get()
                if (sodamToken.refreshToken != refreshToken) {
                    throw TokenException.InvalidTokenException()
                }

                return updateTokenForUser(userId)
            }
        }
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

    private fun getSignKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun parseClaims(accessToken: String): Claims = try {
        Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(accessToken)
            .body
    } catch (e: ExpiredJwtException) {
        e.claims
    }

    private fun getCreateTokenPortByUserType(userType: UserType): CreateTokenPort =
        createTokenPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getFetchTokenPortByUserType(userType: UserType): FetchTokenPort =
        fetchTokenPorts.stream()
            .filter { it.isTarget(userType = userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
