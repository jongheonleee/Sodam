package com.backend.sodam.domain.tags.repository

import com.backend.sodam.domain.tags.entity.TagsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TagJpaRepository : JpaRepository<TagsEntity, Long> {}