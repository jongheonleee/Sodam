package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.entity.QArticleEntity.articleEntity
import com.backend.sodam.domain.articles.entity.QUsersLikeArticleEntity.usersLikeArticleEntity
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.categories.entity.QCategoryEntity.categoryEntity
import com.backend.sodam.domain.subscriptions.entity.QUsersSubscriptionsEntity.usersSubscriptionsEntity
import com.backend.sodam.domain.tags.entity.QTagsEntity.tagsEntity
import com.backend.sodam.domain.users.entity.QSocialUsersEntity.*
import com.backend.sodam.domain.users.entity.QUsersGradeEntity.usersGradeEntity
import com.backend.sodam.domain.users.entity.QUsersPositionsEntity.usersPositionsEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.global.utils.Formatter
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
@RequiredArgsConstructor
class SocialUserCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val formatter: Formatter
) : SocialUserCustomRepository {

    override fun findByProviderIdWithSubscription(providerId: String): Optional<SodamUser>  {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(socialUsersEntity)
            .leftJoin(socialUsersEntity.subscriptions, usersSubscriptionsEntity)
            .where(socialUsersEntity.providerId.eq(providerId)
                .and(usersSubscriptionsEntity.validYN.eq(0))
            )
            .fetchOne()
            ?. let {
                SodamUser(
                    userId = it.socialUserId,
                    username = it.userName,
                    encryptedPassword = "", // 이 부분 어케할지 고민하기
                    introduce = it.introduce,
                    profileImageUrl = "", // 이 부분 어케할지 고민하기
                    email = it.email,
                    role = it.subscriptions.first().subscriptionName.toRole(),
                    userType = UserType.SOCIAL
                )
            }
        )
    }

    @Transactional(readOnly = true)
    override fun findSodamUserByUserId(userId: String): Optional<SodamUser> {
        return Optional.ofNullable(
            jpaQueryFactory.selectFrom(socialUsersEntity)
                .leftJoin(socialUsersEntity.subscriptions, usersSubscriptionsEntity)
                .where(socialUsersEntity.socialUserId.eq(userId)
                    .and(usersSubscriptionsEntity.validYN.eq(0))
                )
                .fetchOne()
                ?. let {
                    SodamUser(
                        userId = it.socialUserId,
                        username = it.userName,
                        encryptedPassword = "", // 이 부분 어케할지 고민하기
                        introduce = it.introduce,
                        profileImageUrl = "", // 이 부분 어케할지 고민하기
                        email = it.email,
                        role = it.subscriptions.first().subscriptionName.toRole(),
                        userType = UserType.SOCIAL
                    )
                }

        )
    }

    @Transactional(readOnly = true)
    override fun findProfileInfoForSocialUser(socialUserId: String): Optional<SodamUserDetail> {
        return Optional.ofNullable(
            jpaQueryFactory.selectFrom(socialUsersEntity)
                .leftJoin(socialUsersEntity.articles, articleEntity)
                .leftJoin(socialUsersEntity.subscriptions, usersSubscriptionsEntity)
                .leftJoin(socialUsersEntity.positions, usersPositionsEntity)
                .leftJoin(socialUsersEntity.grades, usersGradeEntity)
                .where(
                    socialUsersEntity.socialUserId.eq(socialUserId)  // 아이디 비교
                        .and(usersSubscriptionsEntity.validYN.eq(0)) // 구독권 사용 가능한 것
                        .and(usersPositionsEntity.position.validYN.eq(0)) // 포지션 사용 가능한 것
                        .and(usersGradeEntity.validYN.eq(0)) // 사용 가능한 등급
                        .and(
                            usersGradeEntity.startAt.loe(LocalDateTime.now()).and(
                                usersGradeEntity.endAt.goe(
                                    LocalDateTime.now()))) // 현재 시점에 적용되고 있는 등급(선분이력으로 저장)
                )
                .fetchOne()
                ?. let {
                    SodamUserDetail(
                        userId = it.socialUserId,
                        name = it.userName,
                        email = it.email,
                        introduce = it.introduce,
                        profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        subscription = it.subscriptions.first().subscriptionName,
                        articleTotalCnt = it.articles.size.toLong(),
                        grade = it.grades.first().grade!!.gradeName,
                        ranking = 5000,
                        positions = it.positions.mapNotNull { it.position?.positionName }
                    )
                }
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
    override fun findSocialUserOwnLikeArticlesByPageBy(socialUserId: String, pageable: Pageable): Page<SodamArticle> {
        val query = jpaQueryFactory.selectFrom(articleEntity)
            .leftJoin(articleEntity.socialUser, socialUsersEntity)
            .leftJoin(articleEntity.tags, tagsEntity)
            .leftJoin(articleEntity.category, categoryEntity)
            .leftJoin(usersLikeArticleEntity)
            .on(usersLikeArticleEntity.article.eq(articleEntity))
            .where(
                articleEntity.socialUser.socialUserId.eq(socialUserId)
                    .and(usersLikeArticleEntity.socialUser.socialUserId.eq(socialUserId))
            )

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
}