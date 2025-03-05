import Search from "./Search";
import Articles from "./Articles";
import {useState} from "react";
import {ArticleSummaryType} from "../types/article";
import {useParams} from "react-router-dom";

export default function ArticleLikes() {

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
                    alert('게시글 성공적으로 삭제')
                }
            })
            .catch(error => {
                console.error('Error handling delete article');
            })
    }

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