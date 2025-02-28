import {useState} from "react";
import {ArticleProps} from "./ArticleDetail";

interface CommentsProps {
    article: ArticleProps;
}

export default function Comments({ article } : CommentsProps ) {
    const [comment, setComment] = useState("");
    const user = {
        email : "qwefghnm1212@gmail.com"
    }


    const onChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        const {
            target: { name, value },
        } = e;

        if (name === "comment") {
            setComment(value);
        }
    };

    const handleDeleteComment = (comment : any) => {
        alert("dede")
    }

    let onSubmit;
    return (
        <div className="comments">
            <form className="comments__form" onSubmit={onSubmit}>
                <div className="form__block">
                    <label htmlFor="comment">댓글 입력</label>
                    <textarea
                        name="comment"
                        id="comment"
                        required
                        value={comment}
                        onChange={onChange}
                    />
                </div>
                <div className="form__block form__block-reverse">
                    <input type="submit" value="입력" className="form__btn-submit" />
                </div>
            </form>
            <div className="comments__list">
                {article?.comments
                    ?.slice(0)
                    ?.reverse()
                    .map((comment) => (
                        <div key={comment.createdAt} className="comment__box">
                            <div className="comment__profile-box">
                                <img className="article__profile" src={comment?.profileImage} alt="Commentor Profile" />
                                <div className="comment__email">{comment?.email}</div>
                                <div className="comment__date">{comment?.createdAt}</div>
                                {comment.email === user?.email && (
                                    <div
                                        className="comment__delete"
                                        onClick={() => handleDeleteComment(comment)}
                                    >
                                        삭제
                                    </div>
                                )}
                            </div>
                            <div className="comment__text">{comment?.content}</div>
                            <div className="comment__like-box">
                                👍 {comment?.likeCnt} | 👎 {comment?.dislikeCnt}
                            </div>
                        </div>
                    ))}
            </div>
        </div>
    )
}