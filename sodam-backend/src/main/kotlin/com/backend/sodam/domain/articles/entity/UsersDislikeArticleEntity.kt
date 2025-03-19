package com.backend.sodam.domain.articles.entity

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "users_dislike_articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersDislikeArticleEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERS_ARTICLE_DISLIKE_ID")
    val articleDislikeId: Long? = null,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 회원 싫어요 게시글 - 게시글 = 1 : N ✅
    // - 회원 아이디 : 회원 싫어요 게시글 - 회원 = 1 : N ✅
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    val article: ArticleEntity? = null,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null,

) : MutableBaseEntity()
