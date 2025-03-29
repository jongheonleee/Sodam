package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.exception.PositionException
import com.backend.sodam.domain.users.entity.UsersPositionsEntity
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class UserPositionRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val positionJpaRepository: PositionJpaRepository,
    private val userPositionJpaRepository: UserPositionJpaRepository,
) {

    @Transactional
    fun createPositionForUser(userId: String, positionId: String): UsersPositionsEntity {
        val byUserId = userJpaRepository.findByUserId(userId)
        if (byUserId.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val userEntity = byUserId.get()

        val byPositionId = positionJpaRepository.findByPositionId(positionId)
        if (byPositionId.isEmpty) {
            throw PositionException.PositionNotFoundException()
        }

        val positionEntity = byPositionId.get()

        val userPositionEntity = UsersPositionsEntity(
            userPositionId = UUID.randomUUID().toString(),
            user = userEntity,
            position = positionEntity
        )

        return userPositionJpaRepository.save(userPositionEntity)
    }

    @Transactional
    fun createPositionForSocialUser(socialUserId: String, positionName: String): UsersPositionsEntity {
        println(positionName)
        val bySocialUserId = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (bySocialUserId.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val socialUserEntity = bySocialUserId.get()

        val byPositionId = positionJpaRepository.findByPositionName(positionName)
        if (byPositionId.isEmpty) {
            throw PositionException.PositionNotFoundException()
        }

        val positionEntity = byPositionId.get()

        val userPositionEntity = UsersPositionsEntity(
            userPositionId = UUID.randomUUID().toString(),
            socialUser = socialUserEntity,
            position = positionEntity
        )

        return userPositionJpaRepository.save(userPositionEntity)
    }
}