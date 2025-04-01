package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import com.backend.sodam.domain.users.service.port.CreateUserPort
import com.backend.sodam.domain.users.service.port.DeleteUserPort
import com.backend.sodam.domain.users.service.port.FetchUserPort
import com.backend.sodam.domain.users.service.port.UpdateUserPort
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class NormalUserRepository(
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
): CreateUserPort, FetchUserPort, UpdateUserPort, DeleteUserPort {

    override fun isTarget(userType: UserType): Boolean
        = UserType.NORMAL == userType

    @Transactional(readOnly = true)
    override fun isExistsByUserId(userId: String): Boolean {
        return normalUserJpaRepository.existsByUserId(userId)
    }

    @Transactional(readOnly = true)
    override fun isExistsByEmail(email: String): Boolean {
        return normalUserJpaRepository.existsByUserEmail(email)
    }


    // 이 부분 role까지 조회해오게 만들어야함
    @Transactional(readOnly = true)
    override fun findByEmail(email: String): SodamUser {
        if (normalUserJpaRepository.existsByUserEmail(email)) {
            return normalUserJpaRepository.findByEmailWithRole(email).get()
        } else if (socialUserJpaRepository.existsByEmail(email)) {
            val socialUsersEntity = socialUserJpaRepository.findByEmail(email).get()
            return socialUsersEntity.toDomain()
        } else {
            throw UserException.UserNotFoundException()
        }
    }

    @Transactional(readOnly = true)
    fun isExistsByProviderId(providerId: String): Boolean {
        return socialUserJpaRepository.existsByProviderId(providerId)
    }

    @Transactional
    fun createNormalUser(userSignupCommand: UserSignupCommand): SodamUser {
        val signupRequestUserEntity = userSignupCommand.toEntity()
        return normalUserJpaRepository.save(signupRequestUserEntity)
                                      .toDomain()
    }

    @Transactional(readOnly = true)
    fun findSocialUserByProviderId(providerId: String): Optional<SodamUser> {
        val foundSocialUsersEntityOptionalByProviderId = socialUserJpaRepository.findByProviderId(providerId)
        if (foundSocialUsersEntityOptionalByProviderId.isEmpty) {
            return Optional.empty()
        }

        val socialUserEntity = foundSocialUsersEntityOptionalByProviderId.get()
        val foundUserSubscriptionOptionalByProviderId = userSubscriptionRepository.findByUserId(providerId)

        return Optional.of(
            SodamUser(
                userId = socialUserEntity.socialUserId,
                username = socialUserEntity.userName,
                provider = socialUserEntity.provider,
                providerId = socialUserEntity.providerId,
                role = if (foundUserSubscriptionOptionalByProviderId.isPresent) {
                    foundUserSubscriptionOptionalByProviderId.get().subscriptionType.toRole()
                } else {
                    UserSubscription.newSubscription(socialUserEntity.socialUserId).subscriptionType.toRole()
                },
                userType = UserType.SOCIAL
            )
        )
    }

    @Transactional
    fun createSocialUser(
        socialUserSignupCommand: SocialUserSignupCommand
    ): SodamUser {
        val socialUsersEntity = SocialUsersEntity.newEntity(
            userName = socialUserSignupCommand.username,
            provider = socialUserSignupCommand.provider,
            providerId = socialUserSignupCommand.providerId
        )
        return socialUserJpaRepository.save(socialUsersEntity)
            .toDomain()
    }

    // QueryDSL로 바꾸기
    @Transactional(readOnly = true)
    fun findUserByUserId(userId: String): Optional<SodamUser> {
        return normalUserJpaRepository.findSodamUserByUserId(userId)
    }

    @Transactional
    fun updateSocialUser(socialUserId: String, userUpdateCommand: UserUpdateCommand): SodamUser {
        // 아이디로 해당 엔티티를 조회한다.
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(socialUserId).get()
        // 엔티티를 업데이트 한다
        socialUserEntity.update(userUpdateCommand)
        // 이를 저장하고 도메인으로 반환한다.
        return socialUserJpaRepository.save(socialUserEntity)
                                      .toDomain()
    }

    @Transactional
    fun updateNormalUser(userId: String, userUpdateCommand: UserUpdateCommand): SodamUser {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        normalUserEntity.update(userUpdateCommand)
        return normalUserJpaRepository.save(normalUserEntity)
                                .toDomain()
    }

    @Transactional(readOnly = true)
    fun findSocialUserBySocialUserId(userId: String): Optional<SodamUser> {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val foundUserSubscriptionOptionalBySocialUserId = userSubscriptionRepository.findByUserId(socialUserEntity.providerId)

        return Optional.of(
            SodamUser(
                userId = socialUserEntity.socialUserId,
                username = socialUserEntity.userName,
                provider = socialUserEntity.provider,
                providerId = socialUserEntity.providerId,
                role = if (foundUserSubscriptionOptionalBySocialUserId.isPresent) {
                    foundUserSubscriptionOptionalBySocialUserId.get().subscriptionType.toRole()
                } else {
                    UserSubscription.newSubscription(socialUserEntity.socialUserId).subscriptionType.toRole()
                },
                userType = UserType.SOCIAL
            )
        )
    }

    // 문제있는 부분 - 테스트 코드에서 해당 부분 제대로 안돌아감
    @Transactional(readOnly = true)
    override fun findByUserId(userId: String): Optional<SodamUser> {
        val existsUserByUserId = normalUserJpaRepository.existsByUserId(userId)
        if (existsUserByUserId) {
            return findUserByUserId(userId)
        }

        val existsSocialUserBySocialUserId = socialUserJpaRepository.existsBySocialUserId(userId)
        if (existsSocialUserBySocialUserId) {
            return findSocialUserBySocialUserId(userId)
        }

        val existsByProviderId = socialUserJpaRepository.existsByProviderId(userId)
        if (existsByProviderId) {
            return findSocialUserByProviderId(userId)
        }

        return Optional.empty()
    }

    @Transactional(readOnly = true)
    override fun findProfileInfo(userId: String): Optional<SodamUserDetail> {
        return normalUserJpaRepository.findProfileInfoForUser(
            userId = userId
        )
    }

    @Transactional(readOnly = true)
    override fun findOwnArticlesByPageBy(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        return normalUserJpaRepository.findUserOwnArticlesByPageBy( pageable = pageable, userId = userId)
                                      .map {
                                          ArticleSummaryResponse(
                                              articleId = it.articleId,
                                              title = it.title,
                                              username = it.author,
                                              summary = it.summary,
                                              createdAt = it.createdAt,
                                              tags = it.tags
                                          )
                                      }
    }

    @Transactional(readOnly = true)
    override fun findOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        return normalUserJpaRepository.findUserOwnLikeArticlesByPageBy( pageable = pageable, userId = userId)
                                      .map {
                                          ArticleSummaryResponse(
                                              articleId = it.articleId,
                                              title = it.title,
                                              username = it.author,
                                              summary = it.summary,
                                              createdAt = it.createdAt,
                                              tags = it.tags
                                    )
                                }
    }
}
