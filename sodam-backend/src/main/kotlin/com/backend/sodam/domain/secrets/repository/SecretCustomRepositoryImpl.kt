package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.QSecretsEntity.*
import com.backend.sodam.domain.secrets.model.SodamDetailSecret
import com.backend.sodam.domain.secrets.model.SodamSecret
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
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
    private val formatter: Formatter,
) : SecretCustomRepository {

    override fun findByPageBy(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SodamSecret> {
        val query = jpaQueryFactory.selectFrom(secretsEntity)
                                                           .where(
                                                               // 작성자 이름
                                                               secretSearchCommand.author?.let { secretsEntity.secretAuthor.contains(it) },
                                                               // 제목
                                                               secretSearchCommand.title?.let { secretsEntity.secretTitle.contains(it) } )

        // 전체 개수 카운팅
        val totalSecretsCount = query.fetch().size

        // 결과 조회
        val foundResults = query.offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(secretsEntity.secretId.desc())
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
        TODO("Not yet implemented")
    }
}