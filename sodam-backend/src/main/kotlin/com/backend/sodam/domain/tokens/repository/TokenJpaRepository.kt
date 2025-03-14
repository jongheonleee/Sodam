package com.backend.sodam.domain.tokens.repository
import com.backend.sodam.domain.tokens.entity.UsersTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TokenJpaRepository : JpaRepository<UsersTokenEntity, String>, TokenCustomRepository
