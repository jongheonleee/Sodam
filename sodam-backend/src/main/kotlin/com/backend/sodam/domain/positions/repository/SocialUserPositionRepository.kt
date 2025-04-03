package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.service.port.CreateUserPositionPort
import com.backend.sodam.domain.positions.service.port.DeleteUserPositionPort
import com.backend.sodam.domain.positions.service.port.FetchUserPositionPort
import com.backend.sodam.domain.positions.service.port.UpdateUserPositionPort
import com.backend.sodam.domain.users.entity.UsersPositionsEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

// 이거 템플릿 메서드 적용해보기
// NormalUserPositionRepository, SocialUserPositionRepository
@Repository
@RequiredArgsConstructor
class SocialUserPositionRepository(
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val positionJpaRepository: PositionJpaRepository,
    private val userPositionJpaRepository: UsersPositionJpaRepository,
): CreateUserPositionPort, DeleteUserPositionPort, FetchUserPositionPort, UpdateUserPositionPort {

    override fun isTarget(userType: UserType): Boolean
        = UserType.SOCIAL == userType

    @Transactional
    override fun createByPositionId(userId: String, positionId: String) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get() // 다른 부분
        val positionEntity = positionJpaRepository.findByPositionId(positionId).get()
        val userPositionEntity = UsersPositionsEntity(userPositionId = UUID.randomUUID().toString(), socialUser = socialUserEntity, position = positionEntity) // 다른 부분
        userPositionJpaRepository.save(userPositionEntity)
    }

    @Transactional
    override fun createByPositionName(userId: String, positionName: String) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val positionEntity = positionJpaRepository.findByPositionName(positionName).get()
        val userPositionEntity = UsersPositionsEntity(userPositionId = UUID.randomUUID().toString(), socialUser = socialUserEntity, position = positionEntity)
        userPositionJpaRepository.save(userPositionEntity)
    }

    @Transactional
    override fun upsertUserPosition(userId: String, positionId: String): UsersPositionsEntity {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        socialUserEntity.positions.clear()
        val positionEntity = positionJpaRepository.findByPositionId(positionId).get()
        val userPositionEntity = UsersPositionsEntity(userPositionId = UUID.randomUUID().toString(), socialUser = socialUserEntity, position = positionEntity)
        return userPositionJpaRepository.save(userPositionEntity)
    }

}