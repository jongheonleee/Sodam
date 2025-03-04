import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Profile from "../../components/Profile";
import Articles from "../../components/Articles";
import {useEffect, useState} from "react";
import {ArticleSummary} from "../../types/article";


export default function ProfilePage() {
    // 회원 정보
    const user = {
        email : "qwefghnm1212@gmail.com"
    }

    // 게시글
    const [articles, setArticles] = useState<ArticleSummary[]>([]);

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


    return (
        <>
            <Header />
            <Profile />
            <Articles
                articles={articles}
            />
            <Footer />
        </>
    );
}