package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.comments.repository.CommentRepository
import com.backend.sodam.domain.comments.repository.UsersCommentLikeRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CommentLikeService(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val usersLikeCommentRepository: UsersCommentLikeRepository,
) {
    fun handleLike(commentId: Long, userId: String) {
        // [비즈니스 로직]
        // 유저 정보를 조회한다
        // 유저 타입에 따라서 다르게 서로 다르게 적용한다.
        val sodamUserOptional = userRepository.findByUserId(userId)
        if (sodamUserOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = sodamUserOptional.get()

        // 이 부분 해당 빈들을 template method 패턴이나 strategy 패턴 적용해서 list로 순회해서 처리하게 만들 수 있지 않을까?
        // 구조는 아래와 같음
        // template : UsersCommentLikeRepository, details => 1. SocialUserCommentLikeRepository, 2. NormalUserCommentLikeRepository
        // 코드는 아래와 같이 작성할 수 있을 것 같음
        // repositories.stream().filter(repository -> repository.canHandle(sodamUser.userType)).findFirst().orElseThrow().existsCommentLike()
        val isExists = when (sodamUser.userType) {
            UserType.SOCIAL -> {
                usersLikeCommentRepository.existsCommentLikeForSocialUser(
                    commentId = commentId,
                    socialUserId = sodamUser.userId
                )
            }

            else -> {
                usersLikeCommentRepository.existsCommentLikeForUser(
                    commentId = commentId,
                    userId = sodamUser.userId
                )
            }
        }
        // 좋아요 핸들링 처리를 진행한다.
        if (isExists) {
            // 해당 유저가 현재 댓글 좋아요 누른 이력이 있는지 확인한다.
            // 기존에 눌렀다면, 해당 회원 좋아요 댓글 로우를 삭제한다.
            // 해당 댓글의 좋아요 수를 -1 한다.

            // repositories.stream().filter(repository -> repository.canHandle(sodamUser.userType)).findFirst().orElseThrow().deleteCommentLike()
            when (sodamUser.userType) {
                UserType.SOCIAL -> {
                    usersLikeCommentRepository.deleteForSocialUser(
                        commentId = commentId,
                        socialUserId = sodamUser.userId
                    )
                }

                else -> {
                    usersLikeCommentRepository.deleteForUser(
                        commentId = commentId,
                        userId = sodamUser.userId
                    )
                }
            }
            commentRepository.decreaseLikeCnt(commentId)
        } else {
            // 처음 눌렀거나 좋아요 눌렀던 기록이 없다면, 해당 회원 좋아요 댓글 로우를 생성한다.
            // 해당 댓글의 좋아요 수를 +1 한다.


            // repositories.stream().filter(repository -> repository.canHandle(sodamUser.userType)).findFirst().orElseThrow().create()
            when (sodamUser.userType) {
                UserType.SOCIAL -> {
                    usersLikeCommentRepository.createForSocialUser(
                        commentId = commentId,
                        socialUserId = sodamUser.userId
                    )
                }
                else -> {
                    usersLikeCommentRepository.createForUser(
                        commentId = commentId,
                        userId = sodamUser.userId
                    )
                }
            }
            commentRepository.increaseLikeCnt(commentId)
        }

    }
}