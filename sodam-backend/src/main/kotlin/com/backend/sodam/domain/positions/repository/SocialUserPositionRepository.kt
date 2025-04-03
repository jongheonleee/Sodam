package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.exception.PositionException
import com.backend.sodam.domain.positions.service.port.CreateUserPositionPort
import com.backend.sodam.domain.positions.service.port.DeleteUserPositionPort
import com.backend.sodam.domain.positions.service.port.FetchUserPositionPort
import com.backend.sodam.domain.positions.service.port.UpdateUserPositionPort
import com.backend.sodam.domain.users.entity.UsersPositionsEntity
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.service.port.CreateNormalUserPort
import com.backend.sodam.domain.users.service.port.CreateSocialUserPort
import com.backend.sodam.domain.users.service.port.DeleteUserPort
import com.backend.sodam.domain.users.service.port.FetchUserPort
import com.backend.sodam.domain.users.service.port.UpdateUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class SocialUserPositionRepository(
    // 1. 회원 관련 빈 DI
    private val fetchUserPorts: List<FetchUserPort>,
    private val createNormalUserPort: CreateNormalUserPort,
    private val createSocialUserPort: CreateSocialUserPort,
    private val updateUserPorts: List<UpdateUserPort>,
    private val deleteUserPorts: List<DeleteUserPort>,

    // 밑에 부분 위와 같이 쪼개기
    private val userJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val positionJpaRepository: PositionJpaRepository,
    private val userPositionJpaRepository: UsersPositionJpaRepository,
): CreateUserPositionPort, DeleteUserPositionPort, FetchUserPositionPort, UpdateUserPositionPort {

    override fun isTarget(userType: UserType): Boolean
        = UserType.SOCIAL == userType

    @Transactional
    override fun upsertUserPosition(userId: String, positionId: String): UsersPositionsEntity {
        val bySocialUserId = socialUserJpaRepository.findBySocialUserId(userId)
        if (bySocialUserId.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val socialUserEntity = bySocialUserId.get()
        socialUserEntity.positions.clear()

        val byPositionId = positionJpaRepository.findByPositionId(positionId)
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