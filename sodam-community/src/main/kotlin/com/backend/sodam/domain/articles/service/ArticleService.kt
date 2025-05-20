package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand
import com.backend.sodam.domain.articles.service.port.CreateArticlePort
import com.backend.sodam.domain.articles.service.port.DeleteArticlePort
import com.backend.sodam.domain.articles.service.port.FetchArticlePort
import com.backend.sodam.domain.articles.service.port.UpdateArticlePort
import com.backend.sodam.domain.articles.service.response.ArticleCreateResponse
import com.backend.sodam.domain.articles.service.response.ArticleDetailResponse
import com.backend.sodam.domain.articles.service.response.ArticleSimpleResponse
import com.backend.sodam.domain.articles.service.response.ArticleSummaryResponse
import com.backend.sodam.domain.articles.service.response.ArticleUpdateResponse
import com.backend.sodam.domain.articles.service.usecase.CreateArticleUseCase
import com.backend.sodam.domain.articles.service.usecase.DeleteArticleUseCase
import com.backend.sodam.domain.articles.service.usecase.FetchArticleUseCase
import com.backend.sodam.domain.articles.service.usecase.UpdateArticleUseCase
import com.backend.sodam.domain.categories.exception.CategoryException
import com.backend.sodam.domain.categories.service.port.FetchCategoryPort
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
    private val fetchUserPorts: List<FetchUserPort>,
    private val fetchArticlePorts: List<FetchArticlePort>,
    private val createArticlePorts: List<CreateArticlePort>,
    private val updateArticlePorts: List<UpdateArticlePort>,
    private val deleteArticlePorts: List<DeleteArticlePort>,
    private val fetchCategoryPort: FetchCategoryPort,
) : CreateArticleUseCase, FetchArticleUseCase, UpdateArticleUseCase, DeleteArticleUseCase {

    override fun create(userId: String, articleCreateCommand: ArticleCreateCommand): ArticleCreateResponse {
        checkExistsCategory(categoryId = articleCreateCommand.categoryId)
        val userType = extractUserType(userId = userId)
        val articleCreatePort = getArticleCreatePortByUserType(userType)
        return articleCreatePort.createArticle(userId = userId, articleCreateCommand = articleCreateCommand)
            .toArticleCreateResponse()
    }

    override fun fetchFromClient(pageable: Pageable, articleSearchCommand: ArticleSearchCommand): Page<ArticleSummaryResponse> {
        val articleFetchPort = getArticleFetchPort()
        return articleFetchPort.findByPageBy(pageRequest = pageable, articleSearchCommand = articleSearchCommand)
            .map { it.toSummaryResponse() }
    }

    override fun getArticleDetail(articleId: Long): ArticleDetailResponse {
        checkExistsArticle(articleId = articleId)
        val articleUpdatePort = getArticleUpdatePort()
        val articleFetchPort = getArticleFetchPort()
        articleUpdatePort.increaseViewCnt(articleId)
        return articleFetchPort.findDetailByArticleId(articleId)
            .toResponse()
    }

    override fun getArticleSimple(userId: String, articleId: Long): ArticleSimpleResponse {
        checkExistsArticle(articleId = articleId)
        val articleFetchPort = getArticleFetchPort()
        val sodamArticle = articleFetchPort.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(userId)) {
            throw ArticleException.ArticleAccessDeniedException()
        }
        return sodamArticle.toArticleSimpleResponse()
    }

    override fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): ArticleUpdateResponse {
        checkExistsArticle(articleId = articleId)
        checkExistsCategory(categoryId = articleUpdateCommand.categoryId)
        val articleFetchPort = getArticleFetchPort()
        val articleUpdatePort = getArticleUpdatePort()
        val sodamArticle = articleFetchPort.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(articleUpdateCommand.userId)) {
            throw ArticleException.ArticleAccessDeniedException()
        }
        return articleUpdatePort.update(articleId, articleUpdateCommand)
            .toArticleUpdateResponse()
    }

    override fun delete(userId: String, articleId: Long) {
        checkExistsArticle(articleId = articleId)
        val articleFetchPort = getArticleFetchPort()
        val articleDeletePort = getArticleDeletePort()
        val sodamArticle = articleFetchPort.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(userId)) {
            throw ArticleException.ArticleAccessDeniedException()
        }
        articleDeletePort.delete(articleId)
    }

    // 📌 비즈니스 로직 적용 전 작업 유효성 따지는 메서드
    private fun checkExistsArticle(articleId: Long) {
        if (!isExistsArticle(articleId)) {
            throw ArticleException.ArticleNotFoundException()
        }
    }

    private fun checkExistsCategory(categoryId: String) {
        if (!isExistsCategory(categoryId)) {
            throw CategoryException.CategoryNotFoundException()
        }
    }

    private fun isExistsArticle(articleId: Long): Boolean =
        fetchArticlePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
            .isExistsByArticleId(articleId)

    private fun isExistsCategory(categoryId: String): Boolean =
        fetchCategoryPort.isExistsByCategoryId(categoryId = categoryId)

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
        createArticlePorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getArticleFetchPort(): FetchArticlePort =
        fetchArticlePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getArticleUpdatePort(): UpdateArticlePort =
        updateArticlePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getArticleDeletePort(): DeleteArticlePort =
        deleteArticlePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
