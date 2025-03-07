package com.backend.sodam.domain.category.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.UUID


// 카테고리 필드는 대부분이 불변
// - categoryOrd, validYN 이외의 필드는 모두 불변
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class CategoryEntity(
    // 불변 필드
    @Id
    @Column(name = "CATEGORY_ID")
    val categoryId: UUID,

    @Column(name = "TOP_CATEGORY_ID")
    val topCategoryId : UUID,

    @Column(name = "CATEGORY_NAME")
    val categoryName: String,

    // 가변 필드
    categoryOrd: Int,
    validYN: Int,

) : MutableBaseEntity() {

    @Column(name = "CATEGORY_ORD")
    var categoryOrd: Int = categoryOrd
        protected set

    @Column(name = "VALID_YN")
    var validYN: Int = validYN
        protected set
}