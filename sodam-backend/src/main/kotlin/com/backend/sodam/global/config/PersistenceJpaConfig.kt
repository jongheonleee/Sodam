package com.backend.sodam.global.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = ["com.backend.sodam.domain"])
@EnableJpaRepositories(basePackages = ["com.backend.sodam.domain"])
class PersistenceJpaConfig(
    @PersistenceContext
    private val entityManager: EntityManager
) {

    // JpaQueryFactory 빈등록 -> QueryDsl 사용
    @Bean
    fun jpaQueryFactory(entityManager: EntityManager): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}
