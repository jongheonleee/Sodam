package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.repository.ArticleRepository
import com.backend.sodam.domain.articles.repository.UsersArticleLikeRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ArticleLikeService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val usersArticleLikeRepository: UsersArticleLikeRepository,
) {

    fun handleLike(userId: String, articleId: Long) {
        // 근데 유저 타입마다 다르게 처리해야함
        // 사용자가 이미 좋아요를 누른 게시글인지 확인
            // 만약 이미 눌렀던 좋아요 게시글의 경우
            // 게시글 유저 좋아요 테이블에서 행을 삭제함
            // 해당 게시글의 좋아요수 1 빼기
        // 그렇지 않다면, 게시글 유저 싫어요 테이블에 행을 추가함
        // 해당 게시글의 좋아요수 1 증가
        // 근데 유저 타입마다 다르게 처리해야함

        val sodamUserOptional = userRepository.findByUserId(userId)
        if (sodamUserOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = sodamUserOptional.get()
        val isExists = when (sodamUser.userType) {
            UserType.SOCIAL -> {
                usersArticleLikeRepository.existsArticleLikeForSocialUser(
                    articleId = articleId,
                    socialUserId = userId
                )
            }

            else -> {
                usersArticleLikeRepository.existsArticleLikeForUser(
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
                    usersArticleLikeRepository.deleteForSocialUser(articleId, userId)
                }
                else -> {
                    usersArticleLikeRepository.deleteForUser(articleId, userId)
                }
            }
            // 해당 게시글의 좋아요수 1 빼기
            articleRepository.decreaseLikeCnt(articleId)
        } else {
            // 그렇지 않다면, 게시글 유저 싫어요 테이블에 행을 추가함
            when (sodamUser.userType) {
                UserType.SOCIAL -> {
                    usersArticleLikeRepository.createForSocialUser(articleId, userId)
                }
                else -> {
                    usersArticleLikeRepository.createForUser(articleId, userId)
                }
            }
            // 해당 게시글의 좋아요수 1 증가
            articleRepository.increaseLikeCnt(articleId)
        }

    }
}