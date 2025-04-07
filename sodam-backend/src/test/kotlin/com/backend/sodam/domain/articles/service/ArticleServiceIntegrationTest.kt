package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.controller.response.ArticleCreateResponse
import com.backend.sodam.domain.articles.repository.ArticleJpaRepository
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.categories.entity.CategoryEntity
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.subscriptions.repository.NormalUserSubscriptionRepository
import com.backend.sodam.domain.subscriptions.repository.SocialUserSubscriptionRepository
import com.backend.sodam.domain.subscriptions.repository.SubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionJpaRepository
import com.backend.sodam.domain.tags.repository.TagJpaRepository
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class ArticleServiceIntegrationTest(
    private val sut: ArticleService,

    // 테스트 환경에 필요한 빈
    private val tagJpaRepository: TagJpaRepository,
    private val articleJpaRepository: ArticleJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository,
    private val normalUserSubscriptionRepository: NormalUserSubscriptionRepository,
    private val socialUserSubscriptionRepository: SocialUserSubscriptionRepository,
) : BehaviorSpec({

    extension(SpringExtension)

    // 일반회원 목 객체
    val mockNormalUser = UsersEntity(
        userId = UUID.randomUUID().toString(),
        userEmail = "test@test.com",
        userName = "test",
        introduce = "테스트용 일반 유저 목 객체입니다.",
        password = "test",
        profileImageUrl = "프로필 이미지 url",
    )
    // 소셜회원 목 객체
    val mockSocialUser = SocialUsersEntity(
        socialUserId = UUID.randomUUID().toString(),
        provider = "kakao",
        providerId = UUID.randomUUID().toString(),
    )
    // 카테고리 목 객체
    val mockCategory = CategoryEntity(
        categoryId = UUID.randomUUID().toString(),
        topCategoryId = UUID.randomUUID().toString(),
        categoryName = "테스트 카테고리",
        categoryOrd = 1,
        validYN = 0,
    )

    // 구독권 목 객체
    val mockSubscription = SubscriptionsEntity(
        subscriptionId = UUID.randomUUID().toString(),
        subscriptionName = "FREE",
        subscriptionContent = "테스트용 구독권입니다.",
        viewCnt = 0,
        downCnt = 0
    )

    // 테스트 환경 DB 구성
    beforeTest {
        // 게시글 지우기
        tagJpaRepository.deleteAll()
        articleJpaRepository.deleteAll()

        // 카테고리 지우기
        categoryJpaRepository.deleteAll()

        // 회원 지우기
        normalUserJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()

        // 구독권 지우기
        subscriptionJpaRepository.deleteAll()

        // 테이블 초기화 잘 되었는지 확인
        tagJpaRepository.count().shouldBe(0)
        articleJpaRepository.count().shouldBe(0)
        categoryJpaRepository.count().shouldBe(0)
        normalUserJpaRepository.count().shouldBe(0)
        socialUserJpaRepository.count().shouldBe(0)
        subscriptionJpaRepository.count().shouldBe(0)

        // 목 데이터 채워넣기
        categoryJpaRepository.save(mockCategory)
        normalUserJpaRepository.save(mockNormalUser)
        socialUserJpaRepository.save(mockSocialUser)
        subscriptionJpaRepository.save(mockSubscription)

        // 일단 무료 구독권 발급
        normalUserSubscriptionRepository.createSubscription(mockNormalUser.userId, SubscriptionsType.FREE)
    }

    // 테스트 환경 DB 초기화
    afterTest {
        // 게시글 지우기
        tagJpaRepository.deleteAll()
        articleJpaRepository.deleteAll()

        // 카테고리 지우기
        categoryJpaRepository.deleteAll()

        // 발급된 구독권 지우기
        userSubscriptionJpaRepository.deleteAll()

        // 회원 지우기
        normalUserJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()

        // 구독권 지우기
        subscriptionJpaRepository.deleteAll()

        // 테이블 초기화 잘 되었는지 확인
        tagJpaRepository.count().shouldBe(0)
        articleJpaRepository.count().shouldBe(0)
        categoryJpaRepository.count().shouldBe(0)
        normalUserJpaRepository.count().shouldBe(0)
        socialUserJpaRepository.count().shouldBe(0)
        subscriptionJpaRepository.count().shouldBe(0)
        userSubscriptionJpaRepository.count().shouldBe(0)
    }

    given("사용자가 게시글을 생성할 때") {

        `when`("올바른 데이터가 전달되면", {
            val command = ArticleCreateCommand(
                categoryId = mockCategory.categoryId,
                title = "테스트용 게시글",
                summary = "테스트용 게시글 요약글",
                content = "테스트용 게시글입니다.",
                tags = listOf("태그1", "태그2")
            )

            val actual = sut.create(userId = mockNormalUser.userId, articleCreateCommand = command)
            val expected = ArticleCreateResponse(
                articleId = 1,
                title = command.title,
                author = mockNormalUser.userName,
                summary = command.summary,
                content = command.content,
                tags = command.tags,
                createdAt = "...."
            )

            then("게시글을 성공적으로 생성하며 등록된 데이터 일부를 반환한다.") {

                actual.title shouldBe expected.title
                actual.summary shouldBe expected.summary
                actual.content shouldBe expected.content
                actual.tags.size shouldBe expected.tags.size

            }
        })
    }

    given("사용자가 게시글을 검색할 때") {
        `when`("- 1. 타이틀로 검색할 경우", {

            then("그와 관련된 데이터가 조회된다.") {

            }
        })

        `when`("- 2. 작성자로 검색할 경우", {

            then("그와 관련된 데이터가 조회된다.") {

            }
        })

        `when`("- 3. 태그로 검색할 경우", {


            then("그와 관련된 데이터가 조회된다.") {

            }
        })
    }

    given("사용자가 게시글을 상세히 조회할 경우") {

        `when`("해당 게시글이 존재하면", {

            then("게시글을 성공적으로 조회한다.") {

            }
        })

        `when`("해당 게시글이 존재하지 않을 경우", {

            then("ArticleNotFoundException 예외가 발생한다.") {

            }
        })
    }

    given("사용자가 게시글을 수정하는 경우") {

        `when`("해당 게시글이 존재하고 올바른 데이터를 전달할 경우", {

            then("게시글을 성공적으로 수정한다.") {

            }
        })

        `when`("해당 게시글이 존재하지 않는 경우", {

            then("ArticleNotFoundException 예외가 발생한다.") {

            }
        })
    }

    given("사용자가 게시글을 삭제하는 경우") {

        `when`("해당 게시글이 존재하는 경우", {

            then("게시글을 성공적으로 삭제한다.") {

            }
        })

        `when`("해당 게시글이 존재하지 않는 경우", {

            then("ArticleNotFoundException 예외가 발생한다.") {

            }
        })
    }
})
