package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepositoryForNormalUser
import com.backend.sodam.domain.articles.service.port.CreateUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.FetchArticlePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.UpdateArticlePort
import com.backend.sodam.domain.articles.service.port.UpdateUserArticleLikePort
import com.backend.sodam.domain.articles.service.usecase.HandleArticleLikeUseCase
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ArticleLikeService(
    private val fetchUserPorts: List<FetchUserPort>,
    private val fetchUserArticleLikePorts: List<FetchUserArticleLikePort>,
    private val deleteUserArticleLikePorts: List<DeleteUserArticleLikePort>,
    private val createUserArticleLikePorts: List<CreateUserArticleLikePort>,
    private val updateUserArticleLikePorts: List<UpdateArticlePort>,
    private val fetchArticlePorts: List<FetchArticlePort>,
): HandleArticleLikeUseCase {

    // 📌 실제 비즈니스 로직
    override fun handleLike(userId: String, articleId: Long) {
        checkExistsArticle(articleId = articleId)
        val userType = extractUserType(userId = userId)

        val fetchUserArticleLikePort = getFetchUserArticleLikePort(userType)
        val deleteUserArticleLikePort = getDeleteUserArticleLikePort(userType)
        val createUserArticleLikePort = getCreateUserArticleLikePort(userType)
        val updateUserArticleLikePort = getUpdateUserArticleLikePort()

        val isExists = fetchUserArticleLikePort.existsArticleLike(articleId = articleId, userId = userId)
        when(isExists) {
            true -> {
                deleteUserArticleLikePort.deleteLike(articleId = articleId, userId = userId)
                updateUserArticleLikePort.decreaseLikeCnt(articleId)
            }

            false -> {
                createUserArticleLikePort.createLike(articleId = articleId, userId = userId)
                updateUserArticleLikePort.increaseLikeCnt(articleId)
            }
        }
    }

    // 📌 작업 유효성을 검증하는 메서드
    private fun checkExistsArticle(articleId: Long) {
        if (!isExistsArticle(articleId))
            throw ArticleException.ArticleNotFoundException()
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

    private fun getFetchUserArticleLikePort(userType: UserType): FetchUserArticleLikePort =
        fetchUserArticleLikePorts.stream()
                                 .filter { it.isTarget(userType) }
                                 .findFirst()
                                 .orElseThrow { IllegalArgumentException() }

    private fun getDeleteUserArticleLikePort(userType: UserType): DeleteUserArticleLikePort =
        deleteUserArticleLikePorts.stream()
                                  .filter { it.isTarget(userType) }
                                  .findFirst()
                                  .orElseThrow { IllegalArgumentException() }


    private fun getCreateUserArticleLikePort(userType: UserType): CreateUserArticleLikePort =
        createUserArticleLikePorts.stream()
                                  .filter{ it.isTarget(userType) }
                                  .findFirst()
                                  .orElseThrow { IllegalArgumentException() }


    private fun getUpdateUserArticleLikePort(): UpdateArticlePort =
        updateUserArticleLikePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }


    private fun getFetchArticlePort(): FetchArticlePort =
        fetchArticlePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
