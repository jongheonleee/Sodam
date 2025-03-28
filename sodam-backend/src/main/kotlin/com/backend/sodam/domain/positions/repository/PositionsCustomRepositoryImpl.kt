package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.positions.entity.QPositionsEntity
import com.backend.sodam.domain.positions.entity.QPositionsEntity.*
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class PositionsCustomRepositoryImpl(
    private val  jpaQueryFactory: JPAQueryFactory,
): PositionsCustomRepository {

    @Transactional(readOnly = true)
    override fun fetchValidPositionsInOrder(): List<PositionsEntity> {
        return jpaQueryFactory.selectFrom(positionsEntity)
                            .where(positionsEntity.validYN.eq(0))
                            .orderBy(positionsEntity.ord.asc())
                            .fetch()
                            .toList()
    }
}