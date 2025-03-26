import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Profile from "../../components/Profile";
import Articles from "../../components/Articles";
import React, {useEffect, useState} from "react";
import {ArticleSummaryType, CategoryType} from "../../types/article";
import {
    getArticleByKeyword,
    getArticlesByCategoryId,
    getArticlesByPageNumber,
    getArticlesByTag, getArticlesWithCategoryIdAndPageNumber, getUserArticles
} from "../../api/article";
import {getUserInfo} from "../../api/user";
import {UserProfileInfoType} from "../../types/auth";
import Categories, {defaultCategory} from "../../components/Categories";
import {getCategories} from "../../api/category";
import ArticlePagination from "../../components/ArticlePagination";


interface ProfilePageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function ProfilePage({
    handleLogout,
} : ProfilePageProps ) {
    // 회원 정보
    const [ userProfile, setUserProfile ] = useState<UserProfileInfoType | null>(null)

    // 게시글 및 페이징 정보
    const [articles, setArticles] = useState<ArticleSummaryType[]>([])
    const [page, setPage] = useState<number>(1)
    const [totalPages, setTotalPages] = useState<number>(1)

    // 기본 선정 카테고리
    const [activeCategory, setActiveCategory] = useState<CategoryType>(defaultCategory);

    // 카테고리
    const [categories, setCategories] = useState<CategoryType[]>([])

    // 검색 키워드
    const [keyword, setKeyword] = useState<string>('')

    // API 재요청해서 최신 데이터 반영하기
    const [refreshTrigger, setRefreshTrigger] = useState(false)

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
        setCategories([]);


        // 유저가 작성한 게시글 조회
        getUserInfo().then((res) => {
            if (res.status === 200) {
                console.log(res.data.data)
                setUserProfile(res.data.data)
            }
        })

        // 카테고리 조회
        getCategories().then((res) => {
            if (res.status === 200) {
                setCategories(res.data.data.categories)
                console.log(res.data.data)
            }
        })

        // 게시글 조회
        getUserArticles().then((res) => {
            if (res.status === 200) {
                setArticles(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
            }
        })
    }, []);

    // 특정 게시글 삭제 처리 핸들링
    const handleArticleDelete = (id: number) => {
        fetch(`/api/articles/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }
        })
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

    // 카테고리 변경 시 해당 카테고리와 연관있는 게시글 조회
    const handleCategoryChange = (categoryId: string) => {
        let selectedCategory = categories.find(category => category.categoryId === categoryId);
        if (selectedCategory) {
            setActiveCategory(selectedCategory);

            if (selectedCategory.categoryName === '전체') {
                getArticlesByPageNumber(
                    page
                ).then((res) => {
                    if (res.status === 200) {
                        setArticles(res.data.data.content)
                        setPage(res.data.data.pageNumber)
                        setTotalPages(res.data.data.totalPages)
                    }

                    console.log(res.data.data)
                })
            } else {
                getArticlesByCategoryId(
                    selectedCategory.categoryId
                ).then((res) => {
                    if (res.status === 200) {
                        setArticles(res.data.data.content)
                        setPage(res.data.data.pageNumber)
                        setTotalPages(res.data.data.totalPages)
                    }

                    console.log(res.data.data)
                })
            }
        }
    }

    const handlePageChange : (event: unknown, value: number) => void = (event, value) => {
        setPage(value) // 바로 page를 사용할 수 없음
        let selectedCategory = categories.find(category => category.categoryId === activeCategory.categoryId);

        if (selectedCategory && selectedCategory.categoryName !== '전체') {
            setActiveCategory(selectedCategory)
            getArticlesWithCategoryIdAndPageNumber(value, selectedCategory.categoryId).then((res) => {
                if (res.status === 200) {
                    setArticles(res.data.data.content)
                    setPage(res.data.data.pageNumber)
                    setTotalPages(res.data.data.totalPages)
                    console.log(res.data.data)

                }
            })
        } else {
            getArticlesByPageNumber(value).then((res) => {
                if (res.status === 200) {
                    setArticles(res.data.data.content)
                    setPage(res.data.data.pageNumber)
                    setTotalPages(res.data.data.totalPages)
                    console.log(res.data.data)
                }
            })
        }
    }


    // 키워드 검색 시 해당 키워드와 관련된 게시글 조회
    const handleKeywordSearch = (keyword : string) => {
        // 검색 키워드로 게시글 조회
        getArticleByKeyword(keyword).then((res) => {
            if (res.status === 200) {
                setArticles(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
            }

            console.log(res.data.data)
        })
    }

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <Profile
                handleLogout={handleLogout}
                userProfileInfo={userProfile}
            />

            {/* 카테고리 영역 */}

            <Articles
                articles={articles}
                handleArticleDelete={handleArticleDelete}
                handleTagSearch={handleTagSearch}
            />

            <ArticlePagination
                page={page}
                totalPages={totalPages}
                handlePageChange={handlePageChange}
            />

            <Footer />
        </>
    );
}