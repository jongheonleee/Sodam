import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Profile from "../../components/Profile";
import Articles from "../../components/Articles";
import React, {useEffect, useState} from "react";
import {ArticleSummaryType} from "../../types/article";
import {getArticlesByTag} from "../../api/article";


interface ProfilePageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function ProfilePage({
    handleLogout,
} : ProfilePageProps ) {
    // 회원 정보
    const user = {
        email : "qwefghnm1212@gmail.com"
    }

    // 게시글 및 페이징 정보
    const [articles, setArticles] = useState<ArticleSummaryType[]>([])
    const [page, setPage] = useState<number>(1)
    const [totalPages, setTotalPages] = useState<number>(1)

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

    // 컴포넌트 생성시 특정 유저와 관련된 게시글 정보 조회
    useEffect(() => {
        // 초기화
        setArticles([]);


        // 유저가 작성한 게시글 조회
        fetch(`/api/profile/${user.email}`)
            .then(res => res.json())
            .then(articleData => {
                if (articleData?.result === 'SUCCESS') {
                    setArticles(articleData.data);
                }
            })
            .catch(error => {
                console.log('Error fetching data', error);
            });
    }, [user.email]);

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
            <Header
                handleLogout={handleLogout}
            />
            <Profile
                handleLogout={handleLogout}
            />
            <Articles
                articles={articles}
                handleArticleDelete={handleArticleDelete}
                handleTagSearch={handleTagSearch}
            />
            <Footer />
        </>
    );
}