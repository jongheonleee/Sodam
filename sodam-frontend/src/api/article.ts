import axios from "axios";
import {ArticleCreateRequest, ArticleUpdateRequest} from "../types/article";

const client = axios.create({
    baseURL : 'http://localhost:8080', // 백엔드 현재 로컬에서 돌아가고 있음
});


export const getArticlesByPageNumber = (pageNumber : number = 1) => {
    return client.get(`/api/v1/articles?page=${pageNumber-1}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const getArticlesWithCategoryIdAndPageNumber = (pageNumber: number = 1, categoryId: string) => {
    return client.get(`/api/v1/articles?page=${pageNumber-1}&categoryId=${categoryId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const getArticleSimple = (articleId: number) => {
    return client.get(`/api/v1/articles/simple/${articleId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const getArticlesByTag = (tag: string) => {
    return client.get(`/api/v1/articles?tag=${tag}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const getArticleByKeyword = (keyword: string) => {
    return client.get(`/api/v1/articles?keyword=${keyword}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const getArticlesByCategoryId = (categoryId: string) => {
    return client.get(`/api/v1/articles?categoryId=${categoryId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const getDetailArticle = (articleId : string) => {
    return client.get(`/api/v1/articles/${articleId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}


export const postArticle = (articleCreateRequest: ArticleCreateRequest) => {
    return client.post("/api/v1/articles", articleCreateRequest, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const updateArticle = (
    articleId: string,
    articleUpdateRequest: ArticleUpdateRequest
)=> {
    return client.put(`/api/v1/articles/${articleId}`, articleUpdateRequest, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const deleteArticle = (
    articleId: number,
) => {
    return client.delete(`/api/v1/articles/${articleId}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

export const likeArticle = (
    articleId: string,
) => {
    return client.get(`/api/v1/articles/${articleId}/like`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}


export const dislikeArticle = (
    articleId: string,
) => {
    return client.get(`/api/v1/articles/${articleId}/dislike`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}
