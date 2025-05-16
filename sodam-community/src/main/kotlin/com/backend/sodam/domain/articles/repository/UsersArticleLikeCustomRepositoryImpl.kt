package com.backend.sodam.domain.articles.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class UsersArticleLikeCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : UsersArticleLikeCustomRepository
