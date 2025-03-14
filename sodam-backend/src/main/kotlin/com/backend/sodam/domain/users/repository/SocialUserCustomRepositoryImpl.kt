package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.QSocialUsersEntity
import com.backend.sodam.domain.users.entity.QSocialUsersEntity.*
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@RequiredArgsConstructor
class SocialUserCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : SocialUserCustomRepository {

    override fun findByProviderId(providerId: String): Optional<SocialUsersEntity> {
        return jpaQueryFactory.selectFrom(socialUsersEntity)
            .where(socialUsersEntity.providerId.eq(providerId))
            .fetch()
            .stream()
            .findFirst();
    }
}