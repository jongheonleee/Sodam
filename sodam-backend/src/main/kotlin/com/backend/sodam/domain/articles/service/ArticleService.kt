package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepository
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand
import com.backend.sodam.domain.articles.service.response.*
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserRepository
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ArticleService(
    private val userRepository: UserRepository,
    private val socialUserRepository: SocialUserRepository,
    private val articleRepository: ArticleRepository
) {

    fun create(userId: String, articleCreateCommand: ArticleCreateCommand): ArticleCreateResponse {
        val sodamUser = socialUserRepository.findBySocialUserId(userId)
            .orElseGet {
                userRepository.findUserByUserId(userId)
                    .orElseThrow { UserException.UserNotFoundException() }
            }

        val sodamArticle = when (sodamUser.userType) {
            UserType.SOCIAL -> {
                articleRepository.createArticleForSocialUser(
                    userId,
                    articleCreateCommand
                )
            }
            else -> {
                articleRepository.createArticleForUser(
                    userId,
                    articleCreateCommand
                )
            }
        }

        return ArticleCreateResponse(
            articleId = sodamArticle.articleId,
            title = articleCreateCommand.title,
            author = sodamArticle.author,
            summary = sodamArticle.summary,
            content = sodamArticle.content,
            tags = sodamArticle.tags,
            createdAt = sodamArticle.createdAt
        )
    }

    fun fetchFromClient(pageable: Pageable, articleSearchCommand: ArticleSearchCommand): Page<ArticleSummaryResponse> {
        return articleRepository.findByPageBy(
            pageRequest = pageable,
            articleSearchCommand = articleSearchCommand
        ).map {
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

    fun getArticleDetail(articleId: Long): ArticleDetailResponse {
        // 조회수 증가 시키기
        articleRepository.increaseViewCnt(articleId)
        val sodamDetailArticle = articleRepository.findDetailByArticleId(articleId)
        return ArticleDetailResponse(
            userId = sodamDetailArticle.userId,
            articleId = sodamDetailArticle.articleId,
            title = sodamDetailArticle.title,
            profileImageUrl = sodamDetailArticle.profileImageUrl,
            author = sodamDetailArticle.author,
            content = sodamDetailArticle.content,
            createdAt = sodamDetailArticle.createdAt,
            tags = sodamDetailArticle.tags,
            comments = sodamDetailArticle.comments,
            images = sodamDetailArticle.images,
            articleLikeCnt = sodamDetailArticle.articleLikeCnt,
            articleDislikeCnt = sodamDetailArticle.articleDislikeCnt,
            articleViewCnt = sodamDetailArticle.articleViewCnt
        )
    }

    fun getArticleSimple(articleId: Long): ArticleSimpleResponse {
        val sodamArticle = articleRepository.findArticleByArticleId(articleId)
        return ArticleSimpleResponse(
            articleId = sodamArticle.articleId,
        )
    }

    fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): ArticleUpdateResponse {
        val sodamArticle = articleRepository.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(articleUpdateCommand.userId)) { // 수정 권한이 있는지 확인한다.
            throw ArticleException.ArticleAccessDeniedException()
        }

        val sodamUpdatedArticle = articleRepository.update(articleId, articleUpdateCommand) // 해당 게시글을 수정한다.

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
        val sodamArticle = articleRepository.findArticleByArticleId(articleId)
        if (!sodamArticle.canAccess(userId)) {
            throw ArticleException.ArticleAccessDeniedException()
        }

        // 맞다면 삭제 처리
        // - 연관되어 있는 테이블부터 지움(태그, 좋아요, 싫어요, 댓글)
        articleRepository.delete(articleId)
    }
}
