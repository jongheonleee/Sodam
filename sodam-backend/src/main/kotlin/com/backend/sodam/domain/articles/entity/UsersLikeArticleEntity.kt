package com.backend.sodam.domain.articles.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "users_dislike_articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersLikeArticleEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue
    @Column(name = "USERS_ARTICLE_LIKE_ID")
    val articleLikeId: Long

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 회원 싫어요 게시글 - 게시글 = 1 : M
    // - 회원 아이디 : 회원 싫어요 게시글 - 회원 = 1 : N

) : MutableBaseEntity()
