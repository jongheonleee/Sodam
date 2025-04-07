package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepositoryForNormalUser
import com.backend.sodam.domain.articles.service.port.CreateUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleDislikePort
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ArticleDislikeService(
    private val fetchUserPorts: List<FetchUserPort>,
    private val fetchUserArticleDislikePorts: List<FetchUserArticleDislikePort>,
    private val deleteUserArticleDislikePorts: List<DeleteUserArticleDislikePort>,
    private val createUserArticleDislikePorts: List<CreateUserArticleDislikePort>,
    private val articleRepositoryForNormalUser: ArticleRepositoryForNormalUser,
) {

    fun handleDislike(userId: String, articleId: Long) {
        // 작업 유효성 검증
        checkExistsArticle(articleId = articleId)

        // 회원 유형 추출
        val userType = extractUserType(userId)

        // 해당 회원 유형을 처리할 수 있는 포트 조회
        val fetchUserArticleDislikePort = getFetchUserArticleDislikePort(userType)
        val deleteUserArticleDislikePort = getDeleteUserArticleDislikePort(userType)
        val createUserArticleDislikePort = getCreateUserArticleDislikePort(userType)

        // 싫어요 비즈니스 로직
        val isExists = fetchUserArticleDislikePort.existsArticleDislike(articleId = articleId, userId = userId)
        when(isExists) {
            true -> {
                deleteUserArticleDislikePort.deleteDislike(articleId = articleId, userId = userId)
                articleRepositoryForNormalUser.decreaseDislikeCnt(articleId = articleId)
            }

            false -> {
                createUserArticleDislikePort.createDislike(articleId = articleId, userId = userId)
                articleRepositoryForNormalUser.increaseDislikeCnt(articleId = articleId)
            }
        }
    }

    // 📌 작업 유효성을 검증하는 메서드
    private fun checkExistsArticle(articleId: Long) {
        if (!isExistsArticle(articleId))
            throw ArticleException.ArticleNotFoundException()
    }

    private fun isExistsArticle(articleId: Long): Boolean =
        articleRepositoryForNormalUser.isExistsByArticleId(articleId)

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
}
