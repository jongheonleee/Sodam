import {useContext, useState} from "react";
import {Link} from "react-router-dom";
import Comments from "./Comments";

interface ImageProps {
    image: string;
}

interface HashtagProps {
    hashtag: string;
}

interface CommentProps {
    commendId: number;
    profileImage: string;
    email: string;
    createdAt: string;
    content: string;
    likeCnt: number;
    dislikeCnt: number;
}

export interface ArticleProps {
    articleId: number;
    title: string;
    email: string;
    author: string;
    createdAt: string;
    content: string;
    profileImage: string;
    category: string;
    viewCnt: number;
    likeCnt: number;
    dislikeCnt: number;
    images: ImageProps[];
    hashtags: HashtagProps[];
    comments: CommentProps[];
}

export default function ArticleDetail() {
    const article : ArticleProps = {
        articleId : 1,
        title : "테스트용 제목",
        email : "qwefghnm1212@gmail.com",
        author : "yeonuel",
        createdAt : "2025 02. 28. 오후 03:55:31",
        content : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum..",
        profileImage : "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        category : "커리어",
        viewCnt : 100,
        likeCnt : 50,
        dislikeCnt : 12,
        images : [
            {image : "https://images.unsplash.com/photo-1520085601670-ee14aa5fa3e8?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"},
            {image : "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"},
        ],
        hashtags : [
            {hashtag: "커리어"},
            {hashtag: "자기가발"},
        ],
        comments : [
            {
                commendId: 1,
                profileImage : "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email : "wqewqref@gmail.com",
                createdAt : "2025 02. 28. 오후 03:55:31",
                content : "테스트용 댓글 1",
                likeCnt : 4,
                dislikeCnt : 1
            },
            {
                commendId: 2,
                profileImage : "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email : "wqewqref@gmail.com",
                createdAt : "2025 02. 28. 오후 03:55:31",
                content : "테스트용 댓글 2",
                likeCnt : 30,
                dislikeCnt : 6
            },
            {
                commendId: 3,
                profileImage : "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email : "wqewqref@gmail.com",
                createdAt : "2025 02. 28. 오후 03:55:31",
                content : "테스트용 댓글 3",
                likeCnt : 1,
                dislikeCnt : 5
            },
            {
                commendId: 4,
                profileImage : "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                email : "qwefghnm1212@gmail.com",
                createdAt : "2025 02. 28. 오후 03:55:31",
                content : "테스트용 댓글 4",
                likeCnt : 4,
                dislikeCnt : 0
            },

        ]
    }

    const user = {
        email : "qwefghnm1212@gmail.com"
    }

    const handleDelete = () => {
        alert("u clicked!!")
    }

    return (
        <>
            <div className="article__detail">
                {article ? (
                    <>
                        <div className="article__box">
                            <div className="article__title">{article?.title}</div>

                            <div className="article__profile-box">
                                <img className="article__profile" src={article?.profileImage} alt="Author Profile" />
                                <div className="article__author-name">{article?.author}</div>
                                <div className="article__date">{article?.createdAt}</div>
                            </div>

                            <div className="article__utils-box">
                                <div className="article__hashtags-box">
                                    {article.hashtags?.map((hashtag : { hashtag : string }) => (
                                        <span
                                            className="article__hashtag"
                                        >
                                            {hashtag.hashtag}
                                        </span>
                                    ))}
                                </div>

                                {article?.email === user?.email && (
                                    <>
                                        <div
                                            className="article__delete"
                                            role="presentation"
                                            onClick={handleDelete}
                                        >
                                            삭제
                                        </div>
                                        <div className="article__edit">
                                            <Link to={`/article/edit/${article?.articleId}`}>수정</Link>
                                        </div>
                                    </>
                                )}
                            </div>

                            {article?.images?.length > 0 && (
                                <div className="article__image-box">
                                    {article.images.map((img, index) => (
                                        <img key={index} className="article__image" src={img.image} alt={`Article Image ${index}`} />
                                    ))}
                                </div>
                            )}


                            <div className="article__text article__text--pre-wrap">
                                {article?.content}
                            </div>
                        </div>
                        <Comments
                            article={article}
                        />
                    </>
                ) : (
                    <div>게시글이 존재하지 않음</div>
                )}
            </div>
        </>
    )
}