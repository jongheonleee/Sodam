package com.backend.sodam.domain.tokens.repository
import com.backend.sodam.domain.tokens.entity.UsersTokenEntity
import java.util.Optional

interface TokenCustomRepository {
    fun findByUserId(userId: String): Optional<UsersTokenEntity>
    fun findBySocialUserId(socialUserId: String): Optional<UsersTokenEntity>
}
