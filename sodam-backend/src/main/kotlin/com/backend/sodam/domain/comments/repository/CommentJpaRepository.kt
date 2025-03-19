package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentJpaRepository : JpaRepository<CommentEntity, Long>