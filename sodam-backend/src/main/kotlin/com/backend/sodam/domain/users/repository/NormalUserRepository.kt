package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.service.response.ArticleSummaryResponse
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import com.backend.sodam.domain.users.service.port.CreateNormalUserPort
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
    private val normalUserJpaRepository: NormalUserJpaRepository
) : CreateNormalUserPort, FetchUserPort, UpdateUserPort, DeleteUserPort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

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
        return normalUserJpaRepository.findByEmailWithRole(email).get()
    }

    @Transactional
    override fun createUser(userSignupCommand: UserSignupCommand): SodamUser {
        val signupRequestUserEntity = userSignupCommand.toEntity()
        return normalUserJpaRepository.save(signupRequestUserEntity)
            .toDomain()
    }

    // QueryDSL로 바꾸기
    @Transactional(readOnly = true)
    fun findUserByUserId(userId: String): Optional<SodamUser> {
        return normalUserJpaRepository.findSodamUserByUserId(userId)
    }

    @Transactional
    override fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): SodamUser {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        normalUserEntity.update(userUpdateCommand)
        return normalUserJpaRepository.save(normalUserEntity)
            .toDomain()
    }

    // 문제있는 부분 - 테스트 코드에서 해당 부분 제대로 안돌아감
    @Transactional(readOnly = true)
    override fun findByUserId(userId: String): Optional<SodamUser> =
        normalUserJpaRepository.findSodamUserByUserId(userId)

    @Transactional(readOnly = true)
    override fun findProfileInfo(userId: String): Optional<SodamUserDetail> {
        return normalUserJpaRepository.findProfileInfoForUser(userId)
    }

    @Transactional(readOnly = true)
    override fun findOwnArticlesByPageBy(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        return normalUserJpaRepository.findUserOwnArticlesByPageBy(pageable = pageable, userId = userId)
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
        return normalUserJpaRepository.findUserOwnLikeArticlesByPageBy(pageable = pageable, userId = userId)
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
