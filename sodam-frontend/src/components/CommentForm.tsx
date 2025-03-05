
interface CommentFormProps {
    user? : { email : string };
    comment : string;
    handleChange : (e : React.ChangeEvent<HTMLTextAreaElement>) => void;
    handleCommentSubmit : (e: React.FormEvent<HTMLFormElement>) => void;
}

export default function CommentForm({
    user,
    comment,
    handleChange,
    handleCommentSubmit
} : CommentFormProps ) {
    return (
        <form className="comments__form" onSubmit={(e) => handleCommentSubmit(e)}>
            <div className="form__block">
                <label htmlFor="comment">댓글 입력</label>
                <textarea
                    name="comment"
                    id="comment"
                    required
                    value={comment}
                    onChange={(e) => handleChange(e)}
                />
            </div>
            <div className="form__block form__block-reverse">
                <input type="submit" value="입력" className="form__btn-submit" />
            </div>
        </form>
    )
}