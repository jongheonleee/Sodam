package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepository
import com.backend.sodam.domain.articles.service.port.CreateUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleLikePort
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
    private val articleRepository: ArticleRepository, // 이 부분도 추후에 port로 바꿀 예정
) {

    // 📌 실제 비즈니스 로직
    fun handleLike(userId: String, articleId: Long) {
        // 작업 유효성 검증
        checkExistsArticle(articleId = articleId)

        // 회원 유형 추출
        val userType = extractUserType(userId = userId) // 여기서 회원 존재 여부 검증함

        // 해당 회원 유형을 처리할 수 있는 포트 조회
        val fetchUserArticleLikePort = getFetchUserArticleLikePort(userType)
        val deleteUserArticleLikePort = getDeleteUserArticleLikePort(userType)
        val createUserArticleLikePort = getCreateUserArticleLikePort(userType)

        // 좋아요 비즈니스 로직
        val isExists = fetchUserArticleLikePort.existsArticleLike(articleId = articleId, userId = userId)
        when(isExists) {
            true -> {
                deleteUserArticleLikePort.deleteLike(articleId = articleId, userId = userId)
                articleRepository.decreaseLikeCnt(articleId)
            }

            false -> {
                createUserArticleLikePort.createLike(articleId = articleId, userId = userId)
                articleRepository.increaseLikeCnt(articleId)
            }
        }
    }

    // 📌 작업 유효성을 검증하는 메서드
    private fun checkExistsArticle(articleId: Long) {
        if (!isExistsArticle(articleId))
            throw ArticleException.ArticleNotFoundException()
    }

    private fun isExistsArticle(articleId: Long): Boolean =
        articleRepository.isExistsByArticleId(articleId)

    // 📌 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchUserPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }

    // 📌 특정 조건에 부합한 포트 조회용 메서드 - 런타임 시점에 특정 비즈니스 로직을 처리할 수 있는 빈을 선택하는 메서드
    private fun getFetchPortByUserType(userType: UserType): FetchUserPort =
        fetchUserPorts.stream()
                      .filter { it.isTarget(userType) }
                      .findFirst()
                      .orElseThrow { IllegalArgumentException() }

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
}
