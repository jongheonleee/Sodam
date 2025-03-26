package com.backend.sodam.domain.tokens.repository

import com.backend.sodam.domain.tokens.entity.QUsersTokenEntity.usersTokenEntity
import com.backend.sodam.domain.tokens.entity.UsersTokenEntity
import com.backend.sodam.domain.users.entity.QSocialUsersEntity
import com.backend.sodam.domain.users.entity.QSocialUsersEntity.*
import com.backend.sodam.domain.users.entity.QUsersEntity
import com.backend.sodam.domain.users.entity.QUsersEntity.*
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@RequiredArgsConstructor
class TokenCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : TokenCustomRepository {

    override fun findByUserId(userId: String): Optional<UsersTokenEntity> {
        return jpaQueryFactory.selectFrom(usersTokenEntity)
            .leftJoin(usersTokenEntity.user, usersEntity)
            .where(
                usersTokenEntity.user.userId.eq(userId)
            )
            .fetch()
            .stream()
            .findFirst()
    }

    override fun findBySocialUserId(socialUserId: String): Optional<UsersTokenEntity> {
        return jpaQueryFactory.selectFrom(usersTokenEntity)
                              .leftJoin(usersTokenEntity.socialUser, socialUsersEntity)
                              .where(
                                  usersTokenEntity.socialUser.socialUserId.eq(socialUserId)
                              )
                              .fetch()
                              .stream()
                              .findFirst()
    }
}
