package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.service.port.CreateUserPositionPort
import com.backend.sodam.domain.positions.service.port.DeleteUserPositionPort
import com.backend.sodam.domain.positions.service.port.FetchUserPositionPort
import com.backend.sodam.domain.positions.service.port.UpdateUserPositionPort
import com.backend.sodam.domain.users.entity.UsersPositionsEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

// 이거 템플릿 메서드 적용해보기
// NormalUserPositionRepository, SocialUserPositionRepository
@Repository
@RequiredArgsConstructor
class NormalUserPositionRepository(
    private val userJpaRepository: NormalUserJpaRepository,
    private val positionJpaRepository: PositionJpaRepository,
    private val userPositionJpaRepository: UsersPositionJpaRepository,
): CreateUserPositionPort, DeleteUserPositionPort, FetchUserPositionPort, UpdateUserPositionPort {

    override fun isTarget(userType: UserType): Boolean
        = UserType.NORMAL == userType

    @Transactional
    override fun createByPositionId(userId: String, positionId: String) {
        val userEntity = userJpaRepository.findByUserId(userId).get() // 다른 부분
        val positionEntity = positionJpaRepository.findByPositionId(positionId).get()
        val userPositionEntity = UsersPositionsEntity(userPositionId = UUID.randomUUID().toString(), user = userEntity, position = positionEntity) // 다른 부분
        userPositionJpaRepository.save(userPositionEntity)
    }

    @Transactional
    override fun createByPositionName(userId: String, positionName: String) {
        val userEntity = userJpaRepository.findByUserId(userId).get()
        val positionEntity = positionJpaRepository.findByPositionName(positionName).get()
        val userPositionEntity = UsersPositionsEntity(userPositionId = UUID.randomUUID().toString(), user = userEntity, position = positionEntity)
        userPositionJpaRepository.save(userPositionEntity)
    }

    @Transactional
    override fun upsertUserPosition(userId: String, positionId: String): UsersPositionsEntity {
        val userEntity = userJpaRepository.findByUserId(userId).get()
        val positionEntity = positionJpaRepository.findByPositionId(positionId).get()
        userEntity.positions.clear()
        val userPositionEntity = UsersPositionsEntity(userPositionId = UUID.randomUUID().toString(), user = userEntity, position = positionEntity)
        return userPositionJpaRepository.save(userPositionEntity)
    }



}