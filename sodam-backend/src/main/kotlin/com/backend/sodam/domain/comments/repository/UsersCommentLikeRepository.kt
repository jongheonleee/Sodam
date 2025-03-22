package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.UsersLikeCommentEntity
import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class UsersCommentLikeRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val commentJpaRepository: CommentJpaRepository,
    private val usersCommentLikeJpaRepository: UsersCommentLikeJpaRepository
) {

    // 같은 로직이 계속 추가된다는 것은 안좋은 현상의 첫 신호
    // 밑에 코드를 일일이 기본 단위로 쪼개서 처리하기 때문이지 아닐까?
    // 기본 단위 말고 더 큰 단위의 기능으로 묶어서 같은 로직이 반복되는 것을 방지하면 어떨까?
    // 성능의 이점과 가독성 측면도 좋아질 것 같다

    // 존재하는지 여부를 따지는 로직이 중복됨
    // 이 부분을 repository에서 처리하는 것이 아니라
    // service에서 처리하게 맞기고 여기에서는 존재한다는 전재하에 작업 진행하게 만들기
    @Transactional(readOnly = true)
    fun existsCommentLikeForSocialUser(socialUserId: String, commentId: Long): Boolean {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        return usersCommentLikeJpaRepository.existsByCommentAndSocialUser(
            socialUser = foundSocialUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )
    }

    @Transactional(readOnly = true)
    fun existsCommentLikeForUser(userId: String, commentId: Long): Boolean {
        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        return usersCommentLikeJpaRepository.existsByCommentAndUser(
            user = foundUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )
    }

    @Transactional
    fun createForSocialUser(commentId: Long, socialUserId: String) {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val commentLikeEntity = UsersLikeCommentEntity(
            socialUser = foundSocialUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )

        usersCommentLikeJpaRepository.save(commentLikeEntity)
    }

    @Transactional
    fun deleteForSocialUser(commentId: Long, socialUserId: String) {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentLikeEntityOptional = usersCommentLikeJpaRepository.findByCommentAndSocialUser(
            socialUser = foundSocialUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )

        if (foundCommentLikeEntityOptional.isEmpty) {
            throw CommentException.UserLikeCommentNotFoundException()
        }

        val foundCommentLikeEntity = foundCommentLikeEntityOptional.get()
        usersCommentLikeJpaRepository.delete(foundCommentLikeEntity)
    }

    @Transactional
    fun createForUser(commentId: Long, userId: String) {
        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val commentLikeEntity = UsersLikeCommentEntity(
            user = foundUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )

        usersCommentLikeJpaRepository.save(commentLikeEntity)
    }

    @Transactional
    fun deleteForUser(commentId: Long, userId: String) {
        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentLikeEntityOptional = usersCommentLikeJpaRepository.findByCommentAndUser(
            user = foundUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )

        if (foundCommentLikeEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentLikeEntity = foundCommentLikeEntityOptional.get()
        usersCommentLikeJpaRepository.delete(foundCommentLikeEntity)
    }
}
