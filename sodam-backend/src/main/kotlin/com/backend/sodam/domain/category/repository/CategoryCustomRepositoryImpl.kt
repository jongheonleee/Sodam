package com.backend.sodam.domain.category.repository

import com.backend.sodam.domain.category.entity.CategoryEntity
import com.backend.sodam.domain.category.entity.QCategoryEntity.categoryEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class CategoryCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CategoryCustomRepository {

    // 사용 가능한 카테고리를 등록된 정렬 순서에 따라 조회한다.
    override fun fetchValidCategoriesInOrder(): List<CategoryEntity> {
        return jpaQueryFactory.selectFrom(categoryEntity)
                              .where(categoryEntity.validYN.eq(0)) // 사용 가능한 카테고리는 0으로 등록되어 있음
                              .orderBy(categoryEntity.categoryOrd.asc())
                              .fetch()
                              .toList();
    }
}
