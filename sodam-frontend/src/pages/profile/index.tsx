import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Profile from "../../components/Profile";
import Articles from "../../components/Articles";
import {useEffect, useState} from "react";
import {ArticleSummaryType} from "../../types/article";


export default function ProfilePage() {
    // 회원 정보
    const user = {
        email : "qwefghnm1212@gmail.com"
    }

    // 게시글
    const [articles, setArticles] = useState<ArticleSummaryType[]>([]);

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
            <Header />
            <Profile />
            <Articles
                articles={articles}
                handleArticleDelete={handleArticleDelete}
            />
            <Footer />
        </>
    );
}