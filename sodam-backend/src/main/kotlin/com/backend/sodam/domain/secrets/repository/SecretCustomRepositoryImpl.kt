package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.QSecretTagsEntity
import com.backend.sodam.domain.secrets.entity.QSecretsEntity
import com.backend.sodam.domain.secrets.exception.SecretException
import com.backend.sodam.domain.secrets.model.SodamDetailSecret
import com.backend.sodam.domain.secrets.model.SodamSecret
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import com.backend.sodam.domain.tags.service.response.TagResponse
import com.backend.sodam.global.utils.Formatter
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class SecretCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val formatter: Formatter
) : SecretCustomRepository {

    override fun findByPageBy(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SodamSecret> {
        val query = jpaQueryFactory.selectFrom(QSecretsEntity.secretsEntity)
            .where(
                // 작성자 이름
                secretSearchCommand.author?.let { QSecretsEntity.secretsEntity.secretAuthor.contains(it) },
                // 제목
                secretSearchCommand.title?.let { QSecretsEntity.secretsEntity.secretTitle.contains(it) }
            )

        // 전체 개수 카운팅
        val totalSecretsCount = query.fetch().size

        // 결과 조회
        val foundResults = query.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(QSecretsEntity.secretsEntity.secretId.desc())
            .fetch()
            .map {
                SodamSecret(
                    secretId = it.secretId!!,
                    username = it.secretAuthor,
                    title = it.secretTitle,
                    summary = it.secretContent,
                    createdAt = formatter.timeFormat(it.createdAt),
                    thumbnailUrl = it.secretThumbnailUrl,
                    tags = it.tags.map { tag -> tag.tagName }
                )
            }

        return PageImpl(
            foundResults,
            pageable,
            totalSecretsCount.toLong()
        )
    }

    override fun findDetailBySecretId(secretId: Long): SodamDetailSecret {
        return jpaQueryFactory.selectFrom(QSecretsEntity.secretsEntity)
            .leftJoin(QSecretsEntity.secretsEntity.tags, QSecretTagsEntity.secretTagsEntity)
            .where(QSecretsEntity.secretsEntity.secretId.eq(secretId))
            .fetchOne()
            ?.let {
                SodamDetailSecret(
                    secretId = it.secretId!!,
                    author = it.secretAuthor,
                    title = it.secretTitle,
                    content = it.secretContent,
                    createdAt = formatter.timeFormat(it.createdAt),
                    tags = it.tags.map { tag ->
                        TagResponse(
                            articleId = it.secretId!!, // 추후에 secretId로 변경
                            tagId = tag.tagId!!,
                            tagName = tag.tagName,
                        )
                    },
                    secretLikeCnt = it.secretLikeCnt,
                    secretDislikeCnt = it.secretDislikeCnt,
                    secretViewCnt = it.secretViewCnt,
                    comments = listOf(),
                    images = listOf(
                        "https://images.unsplash.com/photo-1520085601670-ee14aa5fa3e8?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                    )
                )
            } ?: throw SecretException.SecretNotFoundException()
    }
}
