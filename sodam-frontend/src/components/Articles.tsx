import { ArticleSummaryType } from "../types/article";
import {Link} from "react-router-dom";

interface ArticlesProps {
    articles: ArticleSummaryType[]
    handleArticleDelete : (articleId: number) => void
    handleTagSearch: (tag: string) => void
}

export default function Articles({
    articles,
    handleArticleDelete,
    handleTagSearch
} : ArticlesProps) {

    return (
        <>
            <div className="article__list">
                {// 아티클이 존재하면 이터레이터 처리하여 그려주기, 그게아니면 게시글 없을을 보여주기
                 articles?.length > 0 ? (
                     articles?.map((article) => (
                         // 각 아티클 단위
                         <div className="article__box">
                             <Link to={`/profile/${article?.username}`}>
                                 <div className="article__profile-box">
                                     <img className="article__profile" src={article.profileImageUrl} alt="Author Profile" />
                                     <div className="article__author-name">{article?.username}</div>
                                     <div className="article__date">{article?.createdAt}</div>
                                 </div>
                             </Link>

                                 <div className="article__tag-box">
                                     {/* 해당 아티클에 태그가 있는 경우 */}
                                     {article.tags?.length > 0 && (
                                         article.tags?.map((tag) => (
                                             <div
                                                 className="article__tag"
                                                 onClick={() => handleTagSearch(tag)}
                                             >{tag}</div>
                                         ))
                                     )}
                                 </div>

                                 {/* 콘텐츠 영역 */}
                             <Link to={`/articles/${article?.articleId}`}>
                                 <div className="article__title">{article?.title}</div>
                                 <div className="article__summary">{article?.summary}</div>
                             </Link>



                             <div className="article__utils-box">
                                 <div
                                    className="article__delete"
                                    role="presentation"
                                    onClick={() => handleArticleDelete(article?.articleId)}
                                 >
                                     삭제
                                 </div>

                                 <Link
                                     to={`/articles/edit/${article?.articleId}`}
                                     className="article__edit">
                                     수정
                                 </Link>

                             </div>

                         </div>
                     ))
                     ) : (
                     <div>게시글이 없습니다.</div>
                 )
                }
            </div>
        </>
    )
}