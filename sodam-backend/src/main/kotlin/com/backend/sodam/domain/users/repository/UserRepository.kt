package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.positions.exception.PositionException
import com.backend.sodam.domain.positions.repository.PositionJpaRepository
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
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class UserRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
) {

    @Transactional(readOnly = true)
    fun isExistsByEmail(email: String): Boolean {
        return userJpaRepository.existsByUserEmail(email)
    }

    @Transactional(readOnly = true)
    fun isExistsByProviderId(providerId: String): Boolean {
        return socialUserJpaRepository.existsByProviderId(providerId)
    }

    @Transactional(readOnly = true)
    fun findByUserEmail(email: String): Optional<SodamUser> {
        val foundUserEntityOptionalByUserEmail = userJpaRepository.findByUserEmail(email)
        if (foundUserEntityOptionalByUserEmail.isEmpty) {
            return Optional.empty()
        }

        val userEntity = foundUserEntityOptionalByUserEmail.get()
        val foundUserSubscriptionOptionalByUserEmail = userSubscriptionRepository.findByUserId(email)

        return Optional.of(
            SodamUser(
                userId = userEntity.userId,
                username = userEntity.userName,
                encryptedPassword = userEntity.password,
                email = userEntity.userEmail,
                introduce = userEntity.userIntroduce,
                profileImageUrl = userEntity.userImage,
                role = if (foundUserSubscriptionOptionalByUserEmail.isPresent) {
                    foundUserSubscriptionOptionalByUserEmail.get().subscriptionType.toRole()
                } else {
                    UserSubscription.newSubscription(userEntity.userId).subscriptionType.toRole()
                },
                userType = UserType.NORMAL
            )
        )
    }

    @Transactional
    fun create(userSignupCommand: UserSignupCommand): SodamUser {
        val signupRequestUserEntity = userSignupCommand.toEntity()
        return userJpaRepository.save(signupRequestUserEntity)
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

    @Transactional(readOnly = true)
    fun findUserByUserId(userId: String): Optional<SodamUser> {
        val foundUserOptionalByUserId = userJpaRepository.findByUserId(userId)

        if (foundUserOptionalByUserId.isEmpty) {
            return Optional.empty()
        }

        val userEntity = foundUserOptionalByUserId.get()
        val foundUserSubscriptionOptionalByUserId = userSubscriptionRepository.findByUserId(userEntity.userEmail)

        return Optional.of(
            SodamUser(
                userId = userEntity.userId,
                username = userEntity.userName,
                encryptedPassword = userEntity.password,
                email = userEntity.userEmail,
                introduce = userEntity.userIntroduce,
                profileImageUrl = userEntity.userImage,
                role = if (foundUserSubscriptionOptionalByUserId.isPresent) {
                    foundUserSubscriptionOptionalByUserId.get().subscriptionType.toRole()
                } else {
                    UserSubscription.newSubscription(userEntity.userId).subscriptionType.toRole()
                },
                userType = UserType.NORMAL
            )
        )
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
        val normalUserEntity = userJpaRepository.findByUserId(userId).get()
        normalUserEntity.update(userUpdateCommand)
        return userJpaRepository.save(normalUserEntity)
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
    fun findByUserId(userId: String): Optional<SodamUser> {
        val existsUserByUserId = userJpaRepository.existsByUserId(userId)
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
    fun findProfileInfo(userId: String): Optional<SodamUserDetail> {
        val sodamUseOptional = findByUserId(userId)
        if (sodamUseOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = sodamUseOptional.get()
        when (sodamUser.userType) {
            UserType.SOCIAL -> {
                return userJpaRepository.findProfileInfoForSocialUser(
                    socialUserId = sodamUser.userId
                )
            }

            else -> {
                return userJpaRepository.findProfileInfoForUser(
                    userId = sodamUser.userId
                )
            }
        }
    }

    @Transactional(readOnly = true)
    fun findOwnArticlesByPageBy(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        val sodamUserOptional = findByUserId(userId)
        if (sodamUserOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = sodamUserOptional.get()

        when (sodamUser.userType) {
            UserType.SOCIAL -> {
                return userJpaRepository.findSocialUserOwnArticlesByPageBy(
                    pageable = pageable,
                    socialUserId = sodamUser.userId
                ).map {
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

            else -> {
                return userJpaRepository.findUserOwnArticlesByPageBy(
                    pageable = pageable,
                    userId = sodamUser.userId
                ).map {
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
    }

    @Transactional(readOnly = true)
    fun findOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        val sodamUserOptional = findByUserId(userId)
        if (sodamUserOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = sodamUserOptional.get()

        when (sodamUser.userType) {
            UserType.SOCIAL -> {
                return userJpaRepository.findSocialUserOwnLikeArticlesByPageBy(
                    pageable = pageable,
                    socialUserId = sodamUser.userId
                ).map {
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

            else -> {
                return userJpaRepository.findUserOwnLikeArticlesByPageBy(
                    pageable = pageable,
                    userId = sodamUser.userId
                ).map {
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
    }
}
