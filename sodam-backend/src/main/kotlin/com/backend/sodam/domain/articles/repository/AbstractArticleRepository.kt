package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.model.SodamDetailArticle
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand
import com.backend.sodam.domain.articles.service.port.CreateArticlePort
import com.backend.sodam.domain.articles.service.port.DeleteArticlePort
import com.backend.sodam.domain.articles.service.port.FetchArticlePort
import com.backend.sodam.domain.articles.service.port.UpdateArticlePort
import com.backend.sodam.domain.categories.exception.CategoryException
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.comments.repository.CommentJpaRepository
import com.backend.sodam.domain.comments.repository.CommentRepository
import com.backend.sodam.domain.tags.entity.TagsEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

// 템플릿 메서드 패턴 적용
// - 게시글 생성은 회원 별로 사뭇 다르지만, 그외의 기능은 같음
abstract class AbstractArticleRepository(
    private val commentRepository: CommentRepository,

    private val articleJpaRepository: ArticleJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
    private val articleLikeJpaRepository: UsersArticleLikeJpaRepository,
    private val articleDislikeJpaRepository: UsersArticleDislikeJpaRepository
): CreateArticlePort, FetchArticlePort, UpdateArticlePort, DeleteArticlePort {

    abstract override fun isTarget(userType: UserType): Boolean
    abstract override fun createArticle(userId: String, articleCreateCommand: ArticleCreateCommand): SodamArticle

    @Transactional(readOnly = true)
    override fun findByPageBy(pageRequest: Pageable, articleSearchCommand: ArticleSearchCommand): Page<SodamArticle> = articleJpaRepository.findByPageBy(
        pageRequest = pageRequest,
        articleSearchCommand = articleSearchCommand
    )

    @Transactional
    open fun increaseViewCnt(articleId: Long) {
        val foundArticleOptionalByArticleId = articleJpaRepository.findByArticleId(articleId)

        if (foundArticleOptionalByArticleId.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }
        val foundArticleEntity = foundArticleOptionalByArticleId.get()
        foundArticleEntity.increaseViewCnt()
    }

    @Transactional
    open fun decreaseLikeCnt(articleId: Long) {
        val foundArticleOptionalByArticleId = articleJpaRepository.findByArticleId(articleId)

        if (foundArticleOptionalByArticleId.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundArticleEntity = foundArticleOptionalByArticleId.get()
        foundArticleEntity.decreaseLikeCnt()
    }

    @Transactional
    open fun increaseLikeCnt(articleId: Long) {
        val foundArticleOptionalByArticleId = articleJpaRepository.findByArticleId(articleId)

        if (foundArticleOptionalByArticleId.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundArticleEntity = foundArticleOptionalByArticleId.get()
        foundArticleEntity.increaseLikeCnt()
    }

    @Transactional
    open fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): SodamArticle {
        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundArticleEntity = foundArticleEntityOptional.get()
        foundArticleEntity.tags.clear() // 연관된 모든 태그 삭제

        val foundCategoryEntityOptionalByCategoryId = categoryJpaRepository.findByCategoryId(articleUpdateCommand.categoryId)
        if (foundCategoryEntityOptionalByCategoryId.isEmpty) {
            throw CategoryException.CategoryNotFoundException()
        } // 카테고리를 조회한다. 없으면 예외 발생

        articleUpdateCommand.tags.map {
            val tagEntity = TagsEntity(tagName = it)
            foundArticleEntity.addTag(tagEntity)
        } // 태그를 다시 생성하고 추가한다.

        foundArticleEntity.update(
            articleUpdateCommand = articleUpdateCommand,
            categoryEntity = foundCategoryEntityOptionalByCategoryId.get()
        ) // 해당 게시글을 업데이트한다.

        return articleJpaRepository.save(foundArticleEntity)
            .toDomain() // 도메인 모델로 반환한다.
    }

    @Transactional
    open fun delete(articleId: Long) {
        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundArticleEntity = foundArticleEntityOptional.get()

        foundArticleEntity.tags.clear()
        foundArticleEntity.comments.forEach {
            commentRepository.delete(it.commentId!!)
        }

        val foundAllArticleLikeByArticle = articleLikeJpaRepository.findByArticle(foundArticleEntity)
        articleLikeJpaRepository.deleteAll(foundAllArticleLikeByArticle)
        val foundArticleDislikeByArticle = articleDislikeJpaRepository.findByArticle(foundArticleEntity)
        articleDislikeJpaRepository.deleteAll(foundArticleDislikeByArticle)
        articleJpaRepository.delete(foundArticleEntity)
    }

    @Transactional(readOnly = true)
    override fun findDetailByArticleId(articleId: Long): SodamDetailArticle = articleJpaRepository.findDetailByArticleId(articleId)

    @Transactional(readOnly = true)
    override fun isExistsByArticleId(articleId: Long): Boolean = articleJpaRepository.findByArticleId(articleId).isPresent

    @Transactional(readOnly = true)
    override fun findArticleByArticleId(articleId: Long): SodamArticle {
        val foundArticleOptionalEntityByArticleId = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleOptionalEntityByArticleId.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        return foundArticleOptionalEntityByArticleId.get()
            .toDomain()
    }

    @Transactional
    open fun decreaseDislikeCnt(articleId: Long) {
        val foundArticleOptionalByArticleId = articleJpaRepository.findByArticleId(articleId)

        if (foundArticleOptionalByArticleId.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundArticleEntity = foundArticleOptionalByArticleId.get()
        foundArticleEntity.decreaseDislikeCnt()
    }

    @Transactional
    open fun increaseDislikeCnt(articleId: Long) {
        val foundArticleOptionalByArticleId = articleJpaRepository.findByArticleId(articleId)

        if (foundArticleOptionalByArticleId.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundArticleEntity = foundArticleOptionalByArticleId.get()
        foundArticleEntity.increaseDislikeCnt()
    }
}
