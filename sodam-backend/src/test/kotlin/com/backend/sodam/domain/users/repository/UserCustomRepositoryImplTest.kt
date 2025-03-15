package com.backend.sodam.domain.users.repository

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserCustomRepositoryImplTest(
    private val sut : UserCustomRepositoryImpl,
    private val userJpaRepository : UserJpaRepository,
) : DescribeSpec({


})