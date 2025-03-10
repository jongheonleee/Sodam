package com.backend.sodam.domain.articles.entity

import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.UUID

@Entity
@Table(name = "article_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ArticleImageEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "ARTICLE_IMAGE_ID")
    val articleImageId: UUID,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 게시글 : 게시글 이미지 = 1 : N ✅
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    var article : ArticleEntity,

    // 가변 필드
    imageUrl: String
) : MutableBaseEntity() {

    @Column(name = "IMAGE_URL")
    var imageUrl = imageUrl
        protected set
}
