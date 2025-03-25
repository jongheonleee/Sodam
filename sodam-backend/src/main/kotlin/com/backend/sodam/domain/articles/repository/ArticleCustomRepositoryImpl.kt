package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.QArticleEntity.articleEntity
import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.model.SodamDetailArticle
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.categories.entity.QCategoryEntity.*
import com.backend.sodam.domain.comments.entity.QCommentEntity.commentEntity
import com.backend.sodam.domain.comments.service.response.CommentResponse
import com.backend.sodam.domain.tags.entity.QTagsEntity.tagsEntity
import com.backend.sodam.domain.tags.service.response.TagResponse
import com.backend.sodam.domain.users.entity.QSocialUsersEntity.*
import com.backend.sodam.domain.users.entity.QUsersEntity.*
import com.backend.sodam.global.utils.Formatter
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class ArticleCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val formatter: Formatter
) : ArticleCustomRepository {

    @Transactional(readOnly = true)
    override fun findByPageBy(
        pageRequest: Pageable,
        articleSearchCommand: ArticleSearchCommand
    ): Page<SodamArticle> {
        // 쿼리 생성
        val query = jpaQueryFactory.selectFrom(articleEntity)
            // 추후에 밑 부분 성능 개선 여지는 없는지 확인해보기
            .leftJoin(articleEntity.tags, tagsEntity)
            .leftJoin(articleEntity.category, categoryEntity)
            .leftJoin(articleEntity.user, usersEntity)
            .leftJoin(articleEntity.socialUser, socialUsersEntity)
            .where(
                // 제목 & 사용자명 확인
                articleSearchCommand.keyword?.let {
                    articleEntity.articleTitle.contains(it)
                        .or(articleEntity.socialUser.userName.eq(it))
                        .or(articleEntity.user.userName.eq(it))
                },

                // 태그명 확인
                articleSearchCommand.tag?.let {
                    tagsEntity.tagName.eq(it)
                },

                // 카테고리 확인
                articleSearchCommand.categoryId?.let {
                    categoryEntity.categoryId.eq(it)
                }
            )

        // 전체 개수 계산
        val totalArticleCount = query.fetch().size

        // 결과 조회
        val foundResults = query.offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .orderBy(articleEntity.createdAt.desc())
            .fetch()
            .map {
                SodamArticle(
                    userId = if (it.user != null) {
                        it.user!!.userId
                    } else {
                        it.socialUser!!.socialUserId
                    },
                    articleId = it.articleId!!,
                    title = it.articleTitle,
                    author = it.name,
                    summary = it.articleSummary,
                    content = it.articleContent,
                    tags = it.tags.map { tag -> tag.tagName },
                    viewCnt = it.articleViewCnt,
                    likeCnt = it.articleLikeCnt,
                    dislikeCnt = it.articleDislikeCnt,
                    createdAt = formatter.timeFormat(it.createdAt)
                )
            }

        return PageImpl(
            foundResults,
            pageRequest,
            totalArticleCount.toLong()
        )
    }

    @Transactional(readOnly = true)
    override fun findDetailByArticleId(articleId: Long): SodamDetailArticle {
        return jpaQueryFactory.selectFrom(articleEntity)
            .leftJoin(articleEntity.tags, tagsEntity)
            .leftJoin(articleEntity.comments, commentEntity)
            .where(articleEntity.articleId.eq(articleId))
            .fetchOne()
            ?.let {
                SodamDetailArticle(
                    userId = it.user?.userId ?: it.socialUser!!.socialUserId,
                    articleId = it.articleId!!,
                    profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    title = it.articleTitle,
                    author = it.user?.userName ?: it.socialUser!!.userName,
                    content = it.articleContent,
                    createdAt = formatter.timeFormat(it.createdAt),
                    tags = it.tags.map { tag ->
                        TagResponse(
                            articleId = it.articleId!!,
                            tagId = tag.tagId!!,
                            tagName = tag.tagName
                        )
                    },
                    articleLikeCnt = it.articleLikeCnt,
                    articleDislikeCnt = it.articleDislikeCnt,
                    articleViewCnt = it.articleViewCnt,
                    comments = it.comments.map { comment ->
                        CommentResponse(
                            commentId = comment.commentId!!,
                            articleId = it.articleId!!,
                            profileImageUrl = comment.userImage!!,
                            userName = comment.user?.userName ?: comment.socialUser!!.userName,
                            createdAt = formatter.timeFormat(comment.createdAt),
                            content = comment.commentContent,
                            commentLikeCnt = comment.commentLikeCnt,
                            commentDislikeCnt = comment.commentDislikeCnt
                        )
                    },
                    images = listOf(
                        "https://images.unsplash.com/photo-1520085601670-ee14aa5fa3e8?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                    )
                )
            } ?: throw ArticleException.ArticleNotFoundException()
    }
}
