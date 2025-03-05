import {CommentType} from "../types/article";

interface CommentListProps {
    user? : {email : string}; // 추후에 변경
    comments : CommentType[];
    handleCommentLike: (id: number) => void,
    handleCommentDislike: (id: number) => void,
    handleCommentDelete: (id : number) => void,
    handleCommentEdit : (id: number) => void,
}

export default function CommentList({
    user,
    comments,
    handleCommentLike,
    handleCommentDislike,
    handleCommentDelete,
    handleCommentEdit,
} : CommentListProps) {
    return (
        <div className="comments__list">
            {comments?.slice(0)
                .reverse()
                .map((comment : CommentType) => (
                    <div key={comment.createdAt} className="comment__box">
                        <div className="comment__profile-box">
                            <img className="article__profile" src={comment?.profileImage.url} alt="Commentor Profile" />
                            <div className="comment__email">{comment?.email}</div>
                            <div className="comment__date">{comment?.createdAt}</div>
                            {comment.email === user?.email && (
                                <>
                                    <div
                                        className="comment__delete"
                                        onClick={() => handleCommentDelete(comment.id)}
                                    >
                                        삭제
                                    </div>

                                    <div
                                        className="comment__delete"
                                        onClick={() => handleCommentEdit(comment.id)}
                                    >
                                        수정
                                    </div>
                                </>
                            )}
                        </div>
                        <div className="comment__text">{comment?.content}</div>
                        <div className="comment__like-box">
                            <button className="comment__like-btn" onClick={() => handleCommentLike(comment.id)}>👍 {comment?.likeCnt || 0}</button>
                            <button className="comment__dislike-btn" onClick={() => handleCommentDislike(comment.id)}>👎 {comment?.dislikeCnt || 0}</button>
                        </div>
                    </div>
                ))}
        </div>
    )
}