import axios from "axios";
import {CommentCreateRequest} from "../types/comment";

const client = axios.create({
    baseURL : 'http://localhost:8080', // 백엔드 현재 로컬에서 돌아가고 있음
});

export const postComment = (articleId: string, commentCreateRequest : CommentCreateRequest) => {
    return client.post(`/api/v1/articles/${articleId}/comments`, commentCreateRequest, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}