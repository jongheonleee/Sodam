package com.backend.sodam.domain.articles.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "article_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ArticleImageEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "ARTICLE_IMAGE_ID")
    val articleImageId: String,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 게시글 : 게시글 이미지 = 1 : N ✅
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    val article: ArticleEntity, // 게시글 불변

    // 가변 필드
    imageUrl: String
) : MutableBaseEntity() {

    @Column(name = "IMAGE_URL")
    var imageUrl = imageUrl
        protected set
}
