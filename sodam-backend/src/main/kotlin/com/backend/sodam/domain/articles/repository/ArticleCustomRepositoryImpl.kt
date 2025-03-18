package com.backend.sodam.domain.articles.repository


import com.backend.sodam.domain.articles.entity.QArticleEntity.articleEntity
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.tags.entity.QTagsEntity.*
import com.backend.sodam.global.utils.Formatter
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class ArticleCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val formatter: Formatter,
) : ArticleCustomRepository {
    override fun findByPageBy(
        pageRequest: Pageable,
        articleSearchCommand: ArticleSearchCommand
    ): Page<SodamArticle> {
        // 쿼리 생성
        val query = jpaQueryFactory.selectFrom(articleEntity)
            .join(articleEntity.tags, tagsEntity) // 태그 조인
            .where(
                // 제목 확인
                articleSearchCommand.title?.let {
                    articleEntity.articleTitle.contains(it)
                },
                // 사용자명 확인
                articleSearchCommand.author?.let {
                    if (articleEntity.user != null) {
                        articleEntity.user.userName.contains(it)
                    } else {
                        articleEntity.socialUser.userName.contains(it)
                    }
                },
                // 태그명 확인
                articleSearchCommand.tag?.let {
                    tagsEntity.tagName.eq(it)
                }
            );


        // 전체 개수 계산
        val totalArticleCount = query.fetch().size

        // 결과 조회
        val foundResults = query.offset(pageRequest.offset)
                                                  .limit(pageRequest.pageSize.toLong())
                                                  .orderBy(articleEntity.createdAt.desc())
                                                  .fetch()
                                                  .map {
                                                        SodamArticle(
                                                            articleId = it.articleId!!,
                                                            title = it.articleTitle,
                                                            author = it.name,
                                                            summary = it.articleSummary,
                                                            content = it.articleContent,
                                                            tags = it.tags.map { tag -> tag.tagName },
                                                            viewCnt = it.articleViewCnt,
                                                            likeCnt = it.articleLikeCnt,
                                                            dislikeCnt = it.articleDislikeCnt,
                                                            createdAt = formatter.timeFormat(it.createdAt),
                                                        )
                                                    }

        return PageImpl(
            foundResults,
            pageRequest,
            totalArticleCount.toLong(),
        )
    }
}