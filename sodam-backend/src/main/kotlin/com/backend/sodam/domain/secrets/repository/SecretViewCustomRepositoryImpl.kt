package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.QViewSecretsEntity.*
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Repository
@RequiredArgsConstructor
class SecretViewCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : SecretViewCustomRepository {

    @Transactional(readOnly = true)
    override fun countViewToday(userId: String): Long {
        val now = LocalDateTime.now()
        val startAt = now.truncatedTo(ChronoUnit.DAYS)
        val endAt = now.plusDays(1)
            .truncatedTo(ChronoUnit.DAYS)

        return jpaQueryFactory.selectFrom(viewSecretsEntity)
            .where(
                viewSecretsEntity.user.userId.eq(userId)
                    .or(viewSecretsEntity.socialUser.socialUserId.eq(userId))
                    .and(viewSecretsEntity.createdAt.goe(startAt)) // startAt <= x < end 범위에 충족하는 데이터 조회
                    .and(viewSecretsEntity.createdAt.lt(endAt))
            )
            .fetch()
            .size
            .toLong()
    }
}
