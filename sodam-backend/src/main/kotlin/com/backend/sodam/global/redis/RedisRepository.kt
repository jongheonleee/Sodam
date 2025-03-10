package com.backend.sodam.global.redis

import lombok.RequiredArgsConstructor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
@RequiredArgsConstructor
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun getValue(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    fun setValue(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    fun setValueTtl(key: String, value: String, ttl: Duration) {
        redisTemplate.opsForValue().set(key, value, ttl)
    }
}