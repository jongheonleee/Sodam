package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.QUsersLikeArticleEntity
import com.backend.sodam.domain.articles.entity.QUsersLikeArticleEntity.*
import com.backend.sodam.domain.articles.entity.UsersLikeArticleEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class UsersArticleLikeCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : UsersArticleLikeCustomRepository {


}