package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.comments.repository.CommentRepository
import com.backend.sodam.domain.comments.repository.UsersCommentDislikeRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CommentDislikeService(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val usersDislikeCommentRepository: UsersCommentDislikeRepository
) {
    fun handleDislike(commentId: Long, userId: String) {
        // [비즈니스 로직]
        // 유저 정보를 조회한다
        // 유저 타입에 따라서 다르게 서로 다르게 적용한다.
        val sodamUserOptional = userRepository.findByUserId(userId)
        if (sodamUserOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = sodamUserOptional.get()
        // 유저 정보를 조회한다
        // 유저 타입에 따라서 다르게 서로 다르게 적용한다.
        // 좋아요 핸들링 처리를 진행한다.
        // 기존에 눌렀다면, 해당 회원 좋아요 댓글 로우를 삭제한다.
        // 해당 댓글의 좋아요 수를 -1 한다.

        // 처음 눌렀거나 좋아요 눌렀던 기록이 없다면, 해당 회원 좋아요 댓글 로우를 생성한다.
        // 해당 댓글의 좋아요 수를 +1 한다.

        val isExists = when (sodamUser.userType) {
            UserType.SOCIAL -> {
                usersDislikeCommentRepository.existsByCommentDislikeForSocialUser(
                    commentId = commentId,
                    socialUserId = sodamUser.userId
                )
            }

            else -> {
                usersDislikeCommentRepository.existsByCommentDislikeForUser(
                    commentId = commentId,
                    userId = sodamUser.userId
                )
            }
        }

        if (isExists) {
            when (sodamUser.userType) {
                UserType.SOCIAL -> {
                    usersDislikeCommentRepository.deleteForSocialUser(
                        commentId = commentId,
                        socialUserId = sodamUser.userId
                    )
                }

                else -> {
                    usersDislikeCommentRepository.deleteForUser(
                        commentId = commentId,
                        userId = sodamUser.userId
                    )
                }
            }

            commentRepository.decreaseDislikeCnt(commentId)
        } else {
            when (sodamUser.userType) {
                UserType.SOCIAL -> {
                    usersDislikeCommentRepository.createForSocialUser(
                        commentId = commentId,
                        socialUserId = sodamUser.userId
                    )
                }

                else -> {
                    usersDislikeCommentRepository.createForUser(
                        commentId = commentId,
                        userId = sodamUser.userId
                    )
                }
            }

            commentRepository.increaseDislikeCnt(commentId)
        }
    }
}
