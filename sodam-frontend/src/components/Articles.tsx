import { ArticleSummary } from "../types/article";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";


export default function Articles() {
    // 게시글 요약 리스트 셋업
    const [articles, setArticles] = useState<ArticleSummary[]>([]);


    // 컴포넌트 생성시 호출(현재 목 api 활용)
    useEffect(() => {
        setArticles([])

        fetch('/api/articles')
            .then((res) => res.json())
            .then((data) => {
                if (data?.result === 'SUCCESS') {
                    setArticles(data.data);
                }}
            )
            .catch((error) => {
                console.log('Error fetching articles', error)
        })
    }, []);



    return (
        <>
            <div className="article__list">
                {// 아티클이 존재하면 이터레이터 처리하여 그려주기, 그게아니면 게시글 없을을 보여주기
                 articles?.length > 0 ? (
                     articles?.map((article) => (
                         // 각 아티클 단위
                         <div className="article__box">
                             {/* 좌측 상단 프로필 영역 */}
                             <Link to={`/profile/${article?.email}`}>
                                 <div className="article__profile-box">
                                     <img className="article__profile" src={article.profileImage.url} alt="Author Profile" />
                                     <div className="article__author-name">{article?.email}</div>
                                     <div className="article__date">{article?.createdAt}</div>
                                 </div>
                             </Link>

                                 {/* 우측 상단 태그 영역 */}
                                 <div className="article__tag-box">
                                     {/* 해당 아티클에 태그가 있는 경우 */}
                                     {article.tags?.length > 0 && (
                                         article.tags?.map((tag) => (
                                             <div className="article__tag">{tag?.name}</div>
                                         ))
                                     )}
                                 </div>

                                 {/* 콘텐츠 영역 */}
                             <Link to={`/articles/${article?.id}`}>
                                 <div className="article__title">
                                     {article?.title}
                                 </div>

                                 <div className="article__summary">
                                     {article?.content}
                                 </div>
                             </Link>



                             {/* 삭제/수정 버튼 영역 */}
                             <div className="article__utils-box">
                                 <div
                                    className="article__delete"
                                    role="presentation"
                                    onClick={() => alert('u click this')}
                                 >
                                     삭제
                                 </div>

                                 <Link
                                     to={`/articles/edit/${article?.id}`}
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