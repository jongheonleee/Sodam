import Search from "./Search";
import Articles from "./Articles";
import {useEffect, useState} from "react";
import {ArticleSummaryType} from "../types/article";
import {useParams} from "react-router-dom";

export default function ArticleLikes()  {
    // 사용자 이메일 조회
    const { email } = useParams();

    // 게시글
    const [articles, setArticles] = useState<ArticleSummaryType[]>([]);

    // 특정 게시글 삭제 처리 핸들링
    const handleArticleDelete = (id: number) => {
        fetch(`/api/articles/${id}`, {
            method : 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }})
            .then(res => res.json())
            .then(data => {
                if (data?.result === 'SUCCESS') {
                    alert('게시글 성공적으로 삭제');
                }
            })
            .catch(error => {
                console.error('Error handling delete article');
            })
    }

    // 사용자 이메일을 통해 해당 사용자가 좋아요를 클릭한 글을 조회해옴
    useEffect(() => {
        // 게시글 초기화
        setArticles([]);

        // 좋아요 게시글 조회
        fetch(`/api/articles/like/${email}`)
            .then(res => res.json())
            .then(data => {
                if (data?.result === 'SUCCESS') {
                    setArticles(data?.data);
                }
            })
            .catch(error => {
                console.error('Error fetching like articles')
            })

    }, [email]);

    return (
        <>
            <Search />
            {/* 콘텐츠 */}
            <Articles
                articles={articles}
                handleArticleDelete={handleArticleDelete}
            />
        </>
    )
}