import {CommentType} from "../types/article";
import CommentForm from "./CommentForm";
import CommentList from "./CommentList";

interface CommentsProps {
    user?: { email : string },
    comments : CommentType[],
    comment : string;
    handleChange : (e : React.ChangeEvent<HTMLTextAreaElement>) => void;
    handleCommentSubmit : (e: React.FormEvent<HTMLFormElement>) => void;
    handleCommentLike: (id: number) => void,
    handleCommentDislike: (id: number) => void,
    handleCommentDelete: (id: number) => void,
    handleCommentEdit : (id: number) => void,
}

export default function Comments({
    user,
    comments,
    comment,
    handleChange,
    handleCommentSubmit,
    handleCommentLike,
    handleCommentDislike,
    handleCommentDelete,
    handleCommentEdit,
} : CommentsProps ) {


    return (
        <div className="comments">
           <CommentForm
               user={user}
               comment={comment}
               handleChange={handleChange}
               handleCommentSubmit={handleCommentSubmit}
           />
            <CommentList
                user={user}
                comments={comments}
                handleCommentLike={handleCommentLike}
                handleCommentDislike={handleCommentDislike}
                handleCommentDelete={handleCommentDelete}
                handleCommentEdit={handleCommentEdit}
            />
        </div>
    )
}