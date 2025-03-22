package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CommentJpaRepository : JpaRepository<CommentEntity, Long> {
    fun findByCommentId(commentId: Long): Optional<CommentEntity>
}
