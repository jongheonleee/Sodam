package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.entity.QArticleEntity.*
import com.backend.sodam.domain.articles.entity.QUsersLikeArticleEntity.*
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.categories.entity.QCategoryEntity.categoryEntity
import com.backend.sodam.domain.subscriptions.entity.QUsersSubscriptionsEntity.*
import com.backend.sodam.domain.tags.entity.QTagsEntity.tagsEntity
import com.backend.sodam.domain.users.entity.QSocialUsersEntity.*
import com.backend.sodam.domain.users.entity.QUsersEntity.*
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.global.utils.Formatter
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
@RequiredArgsConstructor
class UserCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val formatter: Formatter,
) : UserCustomRepository {

    @Transactional(readOnly = true)
    override fun findProfileInfoForSocialUser(socialUserId: String): Optional<SodamUserDetail> {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(socialUsersEntity)
            .leftJoin(socialUsersEntity.articles, articleEntity)
            .leftJoin(socialUsersEntity.subscriptions, usersSubscriptionsEntity)
            .where(
                // 아이디 비교
                socialUsersEntity.socialUserId.eq(socialUserId).and(
                    // 구독권 사용 가능
                    usersSubscriptionsEntity.validYN.eq(0)
                )
            )
            .fetchOne()
            ?. let {
                SodamUserDetail(
                    userId = it.socialUserId,
                    name = it.userName,
                    email = "qwefghnm1212@gmail.com",
                    introduce = "-",
                    profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    subscription = it.subscriptions.stream().findFirst().get().subscriptionName,
                    articleTotalCnt = it.articles.size.toLong(),
                    grade = "신입",
                    ranking = 5000,
                )
            }
        )
    }

    @Transactional(readOnly = true)
    override fun findUserOwnArticlesByPageBy(userId: String, pageable: Pageable): Page<SodamArticle> {
        val query = jpaQueryFactory.selectFrom(articleEntity)
                                                         .leftJoin(articleEntity.user, usersEntity)
                                                         .leftJoin(articleEntity.tags, tagsEntity)
                                                         .leftJoin(articleEntity.category, categoryEntity)
                                                         .where(usersEntity.userId.eq(userId))

        val totalArticleCount = query.fetch().size

        val foundResults = query.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(articleEntity.createdAt.desc())
            .fetch()
            .map {
                SodamArticle(
                    userId = it.user!!.userId,
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
            pageable,
            totalArticleCount.toLong()
        )
    }

    @Transactional(readOnly = true)
    override fun findSocialUserOwnArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle> {
        val query = jpaQueryFactory.selectFrom(articleEntity)
                                                        .leftJoin(articleEntity.socialUser, socialUsersEntity)
                                                        .leftJoin(articleEntity.tags, tagsEntity)
                                                        .leftJoin(articleEntity.category, categoryEntity)
                                                        .where(socialUsersEntity.socialUserId.eq(socialUserId))

        val totalArticleCount = query.fetch().size

        val foundResults = query.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(articleEntity.createdAt.desc())
            .fetch()
            .map {
                SodamArticle(
                    userId = it.socialUser!!.socialUserId,
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
            pageable,
            totalArticleCount.toLong()
        )
    }

    @Transactional(readOnly = true)
    override fun findProfileInfoForUser(userId: String): Optional<SodamUserDetail> {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(usersEntity)
            .leftJoin(usersEntity.articles, articleEntity)
            .leftJoin(usersEntity.subscriptions, usersSubscriptionsEntity)
            .where(
                // 사용자 아이디
                usersEntity.userId.eq(userId).and(
                    // 구독권 사용 여부
                    usersSubscriptionsEntity.validYN.eq(0)
                )
            )
            .fetchOne()
            ?. let {
                SodamUserDetail(
                    userId = it.userId,
                    name = it.userName,
                    email = "qwefghnm1212@gmail.com",
                    introduce = it.userIntroduce,
                    profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    subscription = it.subscriptions.stream().findFirst().get().subscriptionName,
                    articleTotalCnt = it.articles.size.toLong(),
                    grade = "신입",
                    ranking = 5000,
                )
            }
        )
    }

    @Transactional(readOnly = true)
    override fun findSocialUserOwnLikeArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle> {
        val query = jpaQueryFactory.selectFrom(articleEntity)
            .leftJoin(articleEntity.socialUser, socialUsersEntity)
            .leftJoin(articleEntity.tags, tagsEntity)
            .leftJoin(articleEntity.category, categoryEntity)
            .leftJoin(usersLikeArticleEntity)
            .on(usersLikeArticleEntity.article.eq(articleEntity))
            .where(articleEntity.socialUser.socialUserId.eq(socialUserId)
                    .and(usersLikeArticleEntity.socialUser.socialUserId.eq(socialUserId)))

        val totalArticleCount = query.fetch().size

        val foundResults = query.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(articleEntity.createdAt.desc())
            .fetch()
            .map {
                SodamArticle(
                    userId = it.socialUser!!.socialUserId,
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
            pageable,
            totalArticleCount.toLong()
        )
    }

    @Transactional(readOnly = true)
    override fun findUserOwnLikeArticlesByPageBy(userId: String, pageable: Pageable): Page<SodamArticle> {
        val query = jpaQueryFactory.selectFrom(articleEntity)
            .leftJoin(articleEntity.user, usersEntity)
            .leftJoin(articleEntity.tags, tagsEntity)
            .leftJoin(articleEntity.category, categoryEntity)
            .leftJoin(usersLikeArticleEntity)
            .on(usersLikeArticleEntity.article.eq(articleEntity))
            .where(articleEntity.user.userId.eq(userId)
                .and(usersLikeArticleEntity.user.userId.eq(userId)))

        val totalArticleCount = query.fetch().size

        val foundResults = query.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(articleEntity.createdAt.desc())
            .fetch()
            .map {
                SodamArticle(
                    userId = it.user!!.userId,
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
            pageable,
            totalArticleCount.toLong()
        )

    }
}
