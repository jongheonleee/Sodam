package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.comments.repository.CommentRepositoryForNormalUser
import com.backend.sodam.domain.comments.repository.UsersCommentLikeRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CommentLikeService(
    private val fetchUserPorts: List<FetchUserPort>,
    private val commentRepositoryForNormalUser: CommentRepositoryForNormalUser,
    private val usersLikeCommentRepository: UsersCommentLikeRepository
) {
    fun handleLike(commentId: Long, userId: String) {
        // [비즈니스 로직]
        // 유저 정보를 조회한다
        // 유저 타입에 따라서 다르게 서로 다르게 적용한다.
        val userType = extractUserType(userId = userId)

        // 이 부분 해당 빈들을 template method 패턴이나 strategy 패턴 적용해서 list로 순회해서 처리하게 만들 수 있지 않을까?
        // 구조는 아래와 같음
        // template : UsersCommentLikeRepository, details => 1. SocialUserCommentLikeRepository, 2. NormalUserCommentLikeRepository
        // 코드는 아래와 같이 작성할 수 있을 것 같음
        // repositories.stream().filter(repository -> repository.canHandle(sodamUser.userType)).findFirst().orElseThrow().existsCommentLike()
        val isExists = when (userType) {
            UserType.SOCIAL -> {
                usersLikeCommentRepository.existsCommentLikeForSocialUser(
                    commentId = commentId,
                    socialUserId = userId
                )
            }

            else -> {
                usersLikeCommentRepository.existsCommentLikeForUser(
                    commentId = commentId,
                    userId = userId
                )
            }
        }
        // 좋아요 핸들링 처리를 진행한다.
        if (isExists) {
            // 해당 유저가 현재 댓글 좋아요 누른 이력이 있는지 확인한다.
            // 기존에 눌렀다면, 해당 회원 좋아요 댓글 로우를 삭제한다.
            // 해당 댓글의 좋아요 수를 -1 한다.

            // repositories.stream().filter(repository -> repository.canHandle(sodamUser.userType)).findFirst().orElseThrow().deleteCommentLike()
            when (userType) {
                UserType.SOCIAL -> {
                    usersLikeCommentRepository.deleteForSocialUser(
                        commentId = commentId,
                        socialUserId = userId
                    )
                }

                else -> {
                    usersLikeCommentRepository.deleteForUser(
                        commentId = commentId,
                        userId = userId
                    )
                }
            }
            commentRepositoryForNormalUser.decreaseLikeCnt(commentId)
        } else {
            // 처음 눌렀거나 좋아요 눌렀던 기록이 없다면, 해당 회원 좋아요 댓글 로우를 생성한다.
            // 해당 댓글의 좋아요 수를 +1 한다.

            // repositories.stream().filter(repository -> repository.canHandle(sodamUser.userType)).findFirst().orElseThrow().create()
            when (userType) {
                UserType.SOCIAL -> {
                    usersLikeCommentRepository.createForSocialUser(
                        commentId = commentId,
                        socialUserId = userId
                    )
                }
                else -> {
                    usersLikeCommentRepository.createForUser(
                        commentId = commentId,
                        userId = userId
                    )
                }
            }
            commentRepositoryForNormalUser.increaseLikeCnt(commentId)
        }
    }

    // 📌 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }

    // 📌 특정 조건에 부합한 포트 조회용 메서드 - 런타임 시점에 특정 비즈니스 로직을 처리할 수 있는 빈을 선택하는 메서드
    private fun getFetchPortByUserType(userType: UserType): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getFetchPortByUserId(userId: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByUserId(userId) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }
}
