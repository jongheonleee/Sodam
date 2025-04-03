package com.backend.sodam.domain.positions.repository

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SocialUserPositionRepositoryIntegrationTest(
    // 테스트 대상
    private val sut: SocialUserPositionRepository,
) : DescribeSpec({

    // 테스트 과정에서 사용할 목 데이터

    // kotest에서 트랜잭션을 적용하려면 SpringExtension을 사용해야함
    extension(SpringExtension)

    // 테스트 환경 세팅
    beforeTest {

    }
    // 테스트 환경 정리
    afterTest {

    }

    // 테스트 진행
    describe("dd") {

    }
})
