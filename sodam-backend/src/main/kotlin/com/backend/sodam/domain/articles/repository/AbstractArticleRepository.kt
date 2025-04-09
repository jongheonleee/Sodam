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
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.comments.repository.CommentRepositoryForNormalUser
import com.backend.sodam.domain.tags.entity.TagsEntity
import com.backend.sodam.domain.users.model.UserType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional

// 템플릿 메서드 패턴 적용
// - 게시글 생성은 회원 별로 사뭇 다르지만, 그외의 기능은 같음
abstract class AbstractArticleRepository(
    private val commentRepositoryForNormalUser: CommentRepositoryForNormalUser,
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
    override fun increaseViewCnt(articleId: Long) {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        articleEntity.increaseViewCnt()
    }

    @Transactional
    override fun decreaseLikeCnt(articleId: Long) {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        articleEntity.decreaseLikeCnt()
    }

    @Transactional
    override fun increaseLikeCnt(articleId: Long) {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        articleEntity.increaseLikeCnt()
    }

    @Transactional
    override fun update(articleId: Long, articleUpdateCommand: ArticleUpdateCommand): SodamArticle {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val categoryEntity = categoryJpaRepository.findByCategoryId(articleUpdateCommand.categoryId).get()

        articleEntity.tags.clear() // 연관된 모든 태그 삭제
        articleUpdateCommand.tags.map {
            articleEntity.addTag(
                TagsEntity(tagName = it)
            )
        }

        articleEntity.update(
            articleUpdateCommand = articleUpdateCommand,
            categoryEntity = categoryEntity
        )

        return articleJpaRepository.save(articleEntity)
                                   .toDomain()
    }

    @Transactional
    override fun delete(articleId: Long) {
        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()

        articleEntity.tags.clear()
        articleEntity.comments.forEach {
            commentRepositoryForNormalUser.delete(it.commentId!!)
        }

        val foundAllArticleLikeByArticle = articleLikeJpaRepository.findByArticle(articleEntity)
        articleLikeJpaRepository.deleteAll(foundAllArticleLikeByArticle)
        val foundArticleDislikeByArticle = articleDislikeJpaRepository.findByArticle(articleEntity)
        articleDislikeJpaRepository.deleteAll(foundArticleDislikeByArticle)
        articleJpaRepository.delete(articleEntity)
    }

    @Transactional(readOnly = true)
    override fun findDetailByArticleId(articleId: Long): SodamDetailArticle = articleJpaRepository.findDetailByArticleId(articleId)

    @Transactional(readOnly = true)
    override fun isExistsByArticleId(articleId: Long): Boolean = articleJpaRepository.findByArticleId(articleId).isPresent

    @Transactional(readOnly = true)
    override fun findArticleByArticleId(articleId: Long): SodamArticle {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        return articleEntity.toDomain()
    }

    @Transactional
    override fun decreaseDislikeCnt(articleId: Long) {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        articleEntity.decreaseDislikeCnt()
    }

    @Transactional
    override fun increaseDislikeCnt(articleId: Long) {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        articleEntity.increaseDislikeCnt()
    }
}
