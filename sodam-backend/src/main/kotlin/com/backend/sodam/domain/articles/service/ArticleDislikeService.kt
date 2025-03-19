package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.repository.ArticleRepository
import com.backend.sodam.domain.articles.repository.UsersArticleDislikeRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ArticleDislikeService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val usersArticleDislikeRepository: UsersArticleDislikeRepository,
) {

    fun handleDislike(userId: String, articleId: Long) {
        // 사용자가 이미 싫어요를 누른 게시글인지 확인
            // 만약 이미 눌렀던 싫어요 게시글의 경우
            // 게시글 유저 싫어요 테이블에서 행을 삭제함
            // 해당 게시글의 싫어요수 1 빼기
        // 그렇지 않다면, 게시글 유저 싫어요 테이블에 행을 추가함
        // 해당 게시글의 실헝요수 1 증가

        val sodamUserOptional = userRepository.findByUserId(userId)
        if (sodamUserOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = sodamUserOptional.get()

        val isExists = when (sodamUser.userType) {
            UserType.SOCIAL -> {
                usersArticleDislikeRepository.existsArticleDislikeForSocialUser(
                    articleId = articleId,
                    socialUserId = userId
                )
            }

            else -> {
                usersArticleDislikeRepository.existsArticleDislikeForUser(
                    articleId = articleId,
                    userId = userId
                )
            }
        }

        if (isExists) {
            // 이것도 유저 유형마다 다르게 처리해야함
            // 만약 이미 눌렀던 좋아요 게시글의 경우
            // 게시글 유저 좋아요 테이블에서 행을 삭제함
            when (sodamUser.userType) {
                UserType.SOCIAL -> {
                    usersArticleDislikeRepository.deleteForSocialUser(articleId, userId)
                }
                else -> {
                    usersArticleDislikeRepository.deleteForUser(articleId, userId)
                }
            }
            // 해당 게시글의 좋아요수 1 빼기
            articleRepository.decreaseDislikeCnt(articleId)
        } else {
            // 그렇지 않다면, 게시글 유저 싫어요 테이블에 행을 추가함
            when (sodamUser.userType) {
                UserType.SOCIAL -> {
                    usersArticleDislikeRepository.createForSocialUser(articleId, userId)
                }
                else -> {
                    usersArticleDislikeRepository.createForUser(articleId, userId)
                }
            }
            // 해당 게시글의 좋아요수 1 증가
            articleRepository.increaseDislikeCnt(articleId)
        }

    }
}