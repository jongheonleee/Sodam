package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.service.port.CreateUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.FetchArticlePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.UpdateArticlePort
import com.backend.sodam.domain.articles.service.usecase.HandleArticleDislikeUseCase
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class ArticleDislikeService(
    private val fetchUserPorts: List<FetchUserPort>,
    private val fetchUserArticleDislikePorts: List<FetchUserArticleDislikePort>,
    private val deleteUserArticleDislikePorts: List<DeleteUserArticleDislikePort>,
    private val createUserArticleDislikePorts: List<CreateUserArticleDislikePort>,
    private val updateUserArticleDislikePorts: List<UpdateArticlePort>,
    private val fetchArticlePorts: List<FetchArticlePort>
) : HandleArticleDislikeUseCase {

    @Transactional
    override fun handleDislike(userId: String, articleId: Long) {
        checkExistsArticle(articleId = articleId)

        val userType = extractUserType(userId)

        val fetchUserArticleDislikePort = getFetchUserArticleDislikePort(userType)
        val updateArticlePort = getUpdateUserArticleLikePort()

        val isExists = fetchUserArticleDislikePort.existsArticleDislike(articleId = articleId, userId = userId)
        when (isExists) {
            true -> {
                val deleteUserArticleDislikePort = getDeleteUserArticleDislikePort(userType)
                deleteUserArticleDislikePort.deleteDislike(articleId = articleId, userId = userId)
                updateArticlePort.decreaseDislikeCnt(articleId = articleId)
            }

            false -> {
                val createUserArticleDislikePort = getCreateUserArticleDislikePort(userType)
                createUserArticleDislikePort.createDislike(articleId = articleId, userId = userId)
                updateArticlePort.increaseDislikeCnt(articleId = articleId)
            }
        }
    }

    // 📌 작업 유효성을 검증하는 메서드
    private fun checkExistsArticle(articleId: Long) {
        if (!isExistsArticle(articleId)) {
            throw ArticleException.ArticleNotFoundException()
        }
    }

    private fun isExistsArticle(articleId: Long): Boolean =
        getFetchArticlePort().isExistsByArticleId(articleId)

    // 📌 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchUserPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }

    // 📌 특정 조건에 부합한 포트 조회용 메서드 - 런타임 시점에 특정 비즈니스 로직을 처리할 수 있는 빈을 선택하는 메서드
    private fun getFetchUserPortByUserId(userId: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByUserId(userId) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }

    private fun getFetchUserArticleDislikePort(userType: UserType): FetchUserArticleDislikePort =
        fetchUserArticleDislikePorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getCreateUserArticleDislikePort(userType: UserType): CreateUserArticleDislikePort =
        createUserArticleDislikePorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getDeleteUserArticleDislikePort(userType: UserType): DeleteUserArticleDislikePort =
        deleteUserArticleDislikePorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getUpdateUserArticleLikePort(): UpdateArticlePort =
        updateUserArticleDislikePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getFetchArticlePort(): FetchArticlePort =
        fetchArticlePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
