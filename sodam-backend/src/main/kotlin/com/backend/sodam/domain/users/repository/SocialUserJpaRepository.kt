package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SocialUserJpaRepository : JpaRepository<SocialUsersEntity, String>, SocialUserCustomRepository