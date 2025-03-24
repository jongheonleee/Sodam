import Search from "./Search";
import Articles from "./Articles";
import {useEffect, useState} from "react";
import {ArticleSummaryType} from "../types/article";
import {useParams} from "react-router-dom";
import {getArticlesByTag} from "../api/article";

export default function ArticleLikes()  {
    // 게시글 및 페이징 정보
    const [articles, setArticles] = useState<ArticleSummaryType[]>([])
    const [page, setPage] = useState<number>(1)
    const [totalPages, setTotalPages] = useState<number>(1)

    // 사용자 이메일 조회
    const { email } = useParams();

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

    // 태그 선택시 해당 태그로 검색
    const handleTagSearch = (tag : string) => {
        getArticlesByTag(tag).then((res) => {
            if (res.status === 200) {
                setArticles(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
                console.log(res.data.data)
            }
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
                handleTagSearch={handleTagSearch}
            />
        </>
    )
}