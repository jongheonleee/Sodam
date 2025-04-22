package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.UsersLikeCommentEntity
import com.backend.sodam.domain.comments.service.port.CreateUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.DeleteUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.FetchUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.UpdateUserCommentLikePort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class SocialUsersCommentLikeRepository(
    private val commentJpaRepository: CommentJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val usersCommentLikeJpaRepository: UsersCommentLikeJpaRepository
) : CreateUserCommentLikePort, FetchUserCommentLikePort, UpdateUserCommentLikePort, DeleteUserCommentLikePort {

    // 같은 로직이 계속 추가된다는 것은 안좋은 현상의 첫 신호
    // 밑에 코드를 일일이 기본 단위로 쪼개서 처리하기 때문이지 아닐까?
    // 기본 단위 말고 더 큰 단위의 기능으로 묶어서 같은 로직이 반복되는 것을 방지하면 어떨까?
    // 성능의 이점과 가독성 측면도 좋아질 것 같다

    // 존재하는지 여부를 따지는 로직이 중복됨
    // 이 부분을 repository에서 처리하는 것이 아니라
    // service에서 처리하게 맞기고 여기에서는 존재한다는 전재하에 작업 진행하게 만들기
    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional
    override fun createLike(commentId: Long, userId: String) {
        val socialUserEntity = socialUserJpaRepository.findById(userId).get()
        val commentEntity = commentJpaRepository.findById(commentId).get()
        val commentLikeEntity = UsersLikeCommentEntity(
            socialUser = socialUserEntity,
            comment = commentEntity
        )
        usersCommentLikeJpaRepository.save(commentLikeEntity)
    }

    @Transactional(readOnly = true)
    override fun existsCommentLike(commentId: Long, userId: String): Boolean {
        val socialUserEntity = socialUserJpaRepository.findById(userId).get()
        val commentEntity = commentJpaRepository.findById(commentId).get()
        return usersCommentLikeJpaRepository.existsByCommentAndSocialUser(
            socialUser = socialUserEntity,
            comment = commentEntity
        )
    }

    @Transactional
    override fun deleteLike(commentId: Long, userId: String) {
        val socialUserEntity = socialUserJpaRepository.findById(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        val commentLikeEntity = usersCommentLikeJpaRepository.findByCommentAndSocialUser(
            socialUser = socialUserEntity,
            comment = commentEntity
        ).get()
        usersCommentLikeJpaRepository.delete(commentLikeEntity)
    }
}
