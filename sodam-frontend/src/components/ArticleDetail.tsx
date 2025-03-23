import {Link} from "react-router-dom";
import Comments from "./Comments";
import {ArticleDetailType, TagType} from "../types/article";
import React from "react";

interface ArticleDetailProps  {
    user?: { email : string },
    articleDetail: ArticleDetailType,
    comment:string;
    handleChange : (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
    handleArticleDelete: () => void,
    handleArticleLike: () => void,
    handleArticleDislike: () => void,
    handleCommentSubmit : (e: React.FormEvent<HTMLFormElement>) => void;
    handleCommentLike: (id: number) => void,
    handleCommentDislike: (id: number) => void,
    handleCommentDelete: (id : number) => void,
    handleCommentEdit : (id: number) => void,
}

export default function ArticleDetail({
    user,
    articleDetail,
    comment,
    handleChange,
    handleArticleDelete,
    handleArticleLike,
    handleArticleDislike,
    handleCommentSubmit,
    handleCommentLike,
    handleCommentDislike,
    handleCommentDelete,
    handleCommentEdit,
}: ArticleDetailProps)  {
    return (
        <>
            <div className="article__detail">
                {articleDetail ? (
                    <>
                        <div className="article__box">
                            <div className="article__title">{articleDetail?.title}</div>

                            <div className="article__profile-box">
                                <img className="article__profile" src={articleDetail?.profileImageUrl} alt="Author Profile" />
                                <div className="article__author-name">{articleDetail?.author}</div>
                                <div className="article__date">{articleDetail?.createdAt}</div>
                                <div className="article__view-cnt"> 조회수 : {articleDetail?.articleViewCnt}</div>
                            </div>

                            <div className="article__utils-box">
                                <div className="article__hashtags-box">
                                    {articleDetail.tags?.map((tag : TagType ) => (
                                        <span
                                            key={tag.tagId}
                                            className="article__hashtag"
                                        >
                                            {tag.tagName}
                                        </span>
                                    ))}
                                </div>

                                    {/*일단 모든 유저에게 삭제, 수정 버튼 보여주기. 인가 처리는 백엔드에서 처리하기 */}
                                <>
                                    <div
                                        className="article__delete"
                                        role="presentation"
                                        onClick={handleArticleDelete}
                                    >
                                        삭제
                                    </div>
                                    <div className="article__edit">
                                        <Link to={`/articles/edit/${articleDetail?.articleId}`}>수정</Link>
                                    </div>
                                </>
                            </div>

                            {articleDetail?.images?.length > 0 && (
                                <div className="article__image-box">
                                    {articleDetail.images.map((img) => (
                                        <img className="article__image" src={img}  />
                                    ))}
                                </div>
                            )}


                            <div className="article__text article__text--pre-wrap">
                                {articleDetail?.content}
                            </div>

                            {/* 좋아요 & 싫어요 버튼 추가 */}
                            <div className="article__reaction-box">
                                <button className="article__like-btn" onClick={handleArticleLike}>👍 {articleDetail?.articleLikeCnt || 0}</button>
                                <button className="article__dislike-btn" onClick={handleArticleDislike}>👎 {articleDetail?.articleDislikeCnt || 0}</button>
                            </div>


                        </div>
                        <Comments
                            user={user}
                            comments={articleDetail?.comments}
                            comment={comment}
                            handleChange={handleChange}
                            handleCommentSubmit={handleCommentSubmit}
                            handleCommentLike={handleCommentLike}
                            handleCommentDislike={handleCommentDislike}
                            handleCommentDelete={handleCommentDelete}
                            handleCommentEdit={handleCommentEdit}
                        />
                    </>
                ) : (
                    <div>게시글이 존재하지 않음</div>
                )}
            </div>
        </>
    )
}