package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.controller.response.ArticleCreateResponse
import com.backend.sodam.domain.articles.controller.response.ArticleDetailResponse
import com.backend.sodam.domain.articles.controller.response.ArticleSimpleResponse
import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.articles.controller.response.ArticleUpdateResponse
import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepositoryForNormalUser
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand
import com.backend.sodam.domain.articles.service.port.CreateArticlePort
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ArticleService(
    // 회원
    private val fetchUserPorts: List<FetchUserPort>,

    // 게시글
    private val createArticlePort: List<CreateArticlePort>,

    private val articleRepositoryForNormalUser: ArticleRepositoryForNormalUser
) {

    fun create(userId: String, articleCreateCommand: ArticleCreateCommand): ArticleCreateResponse {
        val userType = extractUserType(userId)
        val articleCreatePort = getArticleCreatePortByUserType(userType)
        val sodamArticle  = articleCreatePort.createArticle(userId = userId, articleCreateCommand = articleCreateCommand)
        return sodamArticle.toArticleCreateResponse()
    }

    fun fetchFromClient(pageable: Pageable, articleSearchCommand: ArticleSearchCommand): Page<ArticleSummaryResponse> {
        return articleRepositoryForNormalUser.findByPageBy(pageRequest = pageable, articleSearchCommand = articleSearchCommand)
                                             .map { it.toSummaryResponse() }
    }

    fun getArticleDetail(articleId: Long): ArticleDetailResponse {
        articleRepositoryForNormalUser.increaseViewCnt(articleId)
        val sodamDetailArticle = articleRepositoryForNormalUser.findDetailByArticleId(articleId)
        return sodamDetailArticle.toResponse()
    }

    fun getArticleSimple(userId: String, articleId: Long): ArticleSimpleResponse {
        val sodamArticle = articleRepositoryForNormalUser.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(userId)) {
            throw ArticleException.ArticleAccessDeniedException()
        }

        return ArticleSimpleResponse(
            articleId = sodamArticle.articleId,
            title = sodamArticle.title,
            summary = sodamArticle.summary,
            content = sodamArticle.content
        )
    }

    fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): ArticleUpdateResponse {
        val sodamArticle = articleRepositoryForNormalUser.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(articleUpdateCommand.userId)) { // 수정 권한이 있는지 확인한다.
            throw ArticleException.ArticleAccessDeniedException()
        }

        val sodamUpdatedArticle = articleRepositoryForNormalUser.update(articleId, articleUpdateCommand) // 해당 게시글을 수정한다.

        return ArticleUpdateResponse( // 수정된 결과를 반환한다.
            articleId = sodamUpdatedArticle.articleId,
            title = sodamUpdatedArticle.title,
            author = sodamUpdatedArticle.author,
            summary = sodamUpdatedArticle.summary,
            content = sodamUpdatedArticle.content,
            tags = sodamUpdatedArticle.tags,
            createdAt = sodamUpdatedArticle.createdAt
        )
    }

    fun delete(userId: String, articleId: Long) {
        // userId 가 작성한 글이 맞는지 확인
        val sodamArticle = articleRepositoryForNormalUser.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(userId)) {
            throw ArticleException.ArticleAccessDeniedException()
        }

        // 맞다면 삭제 처리
        // - 연관되어 있는 테이블부터 지움(태그, 좋아요, 싫어요, 댓글)
        articleRepositoryForNormalUser.delete(articleId)
    }

    // 📌 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }


    // 📌 특정 조건에 부합한 포트 조회용 메서드 - 런타임 시점에 특정 비즈니스 로직을 처리할 수 있는 빈을 선택하는 메서드
    private fun getFetchPortByUserId(userId: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByUserId(userId) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }

    private fun getArticleCreatePortByUserType(userType: UserType): CreateArticlePort =
        createArticlePort.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow{ IllegalArgumentException() }

}
