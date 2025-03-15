package com.backend.sodam.domain.tokens.repository

import com.backend.sodam.domain.tokens.entity.UsersTokenEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class TokenCustomRepositoryImplTest(
    private val sut : TokenCustomRepositoryImpl,
    private val tokenJpaRepository: TokenJpaRepository,
    private val userJpaRepository : UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
) : DescribeSpec({

    val userId = UUID.randomUUID().toString()
    val socialUserId = UUID.randomUUID().toString()
    var savedUsersTokenEntity : UsersTokenEntity? = null
    var savedSocialUserTokenEntity : UsersTokenEntity? = null

    // 테스트 환경 구축
    beforeEach {
        // 테스트에서 사용할 DB 초기화
        tokenJpaRepository.deleteAll()
        userJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()

        // 1. 회원 생성 및 해당 회원의 토큰 저장
        val usersEntity = UsersEntity(
            userId = userId,
            userName = "테스트 유저",
            userEmail = "test@test.com",
            introduce = "테스트 유저 자기 소개글",
            profileImageUrl = "avatarUrl",
            password = "password",
        )
        val savedUserEntity = userJpaRepository.save(usersEntity)

        val usersTokenEntity = UsersTokenEntity(
            tokenId = UUID.randomUUID().toString(),
            user = savedUserEntity,
            accessToken = "accessToken1",
            refreshToken = "refreshToken1",
            accessTokenExpiresAt = LocalDateTime.now().plusHours(5),
            refreshTokenExpiresAt = LocalDateTime.now().plusHours(24),
        )
        savedUsersTokenEntity = tokenJpaRepository.save(usersTokenEntity)


        // 2. 소셜 회원 생성 및 해당 회원의 토큰 저장
        val socialUserEntity = SocialUsersEntity(
            socialUserId = socialUserId,
            provider = "provider",
            providerId = "프로바이더 아이디",
            userName = "테스트 유저"
        )
        val savedSocialUserEntity = socialUserJpaRepository.save(socialUserEntity)

        val socialUserTokenEntity = UsersTokenEntity(
            tokenId = UUID.randomUUID().toString(),
            socialUser = savedSocialUserEntity,
            accessToken = "accessToken2",
            refreshToken = "refreshToken2",
            accessTokenExpiresAt = LocalDateTime.now().plusHours(5),
            refreshTokenExpiresAt = LocalDateTime.now().plusHours(24),
        )

        savedSocialUserTokenEntity = tokenJpaRepository.save(socialUserTokenEntity)
    }

    describe("아이디로 회원 조회시") {
        context("아이디에 해당하는 회원의 토큰이 존재하는 경우") {
            it("회원 토큰을 반환한다.") {
                val actual = sut.findByUserId(userId)
                actual.isPresent shouldBe true

                val actualUsersTokenEntity = actual.get()

                actualUsersTokenEntity.tokenId shouldBe savedUsersTokenEntity?.tokenId
                actualUsersTokenEntity.accessToken shouldBe savedUsersTokenEntity?.accessToken
                actualUsersTokenEntity.refreshToken shouldBe savedUsersTokenEntity?.refreshToken
                actualUsersTokenEntity.accessTokenExpiresAt shouldBe savedUsersTokenEntity?.accessTokenExpiresAt
                actualUsersTokenEntity.refreshToken shouldBe savedUsersTokenEntity?.refreshToken
            }
        }

        context("아이디에 해당하는 회원의 토큰이 존재하지 않는 경우") {
            val notExistsUserId = UUID.randomUUID().toString()

            it("회원 토큰을 반환하지 않는다.") {
                val actual = sut.findByUserId(notExistsUserId)
                actual.isEmpty shouldBe true
            }
        }
    }

    describe("소셜 아이디로 회원 조회") {
        context("소셜 아이디에 해당하는 회원의 토큰이 존재하는 경우") {
            it("회원 토큰을 반환한다.") {
                val actual = sut.findBySocialUserId(socialUserId)
                actual.isPresent shouldBe true

                val actualSocialUserTokenEntity = actual.get()

                actualSocialUserTokenEntity.tokenId shouldBe savedSocialUserTokenEntity?.tokenId
                actualSocialUserTokenEntity.accessToken shouldBe savedSocialUserTokenEntity?.accessToken
                actualSocialUserTokenEntity.refreshToken shouldBe savedSocialUserTokenEntity?.refreshToken
                actualSocialUserTokenEntity.accessTokenExpiresAt shouldBe savedSocialUserTokenEntity?.accessTokenExpiresAt
                actualSocialUserTokenEntity.refreshToken shouldBe savedSocialUserTokenEntity?.refreshToken

            }
        }

        context("소셜 아이디에 해당하는 회원의 토큰이 존재하지 않는 경우") {
            val notExistsSocialUserId = UUID.randomUUID().toString()

            it("회원 토큰을 반환하지 않는다.") {
                val actual = sut.findBySocialUserId(notExistsSocialUserId)
                actual.isEmpty shouldBe true
            }
        }
    }
})