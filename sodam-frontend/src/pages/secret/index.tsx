import Header from "../../components/Header";
import Footer from "../../components/Footer";
import SubscriptionCarousel from "../../components/SubscriptionCarousel";
import Secretes from "../../components/Secretes";
import React, {useEffect, useState} from "react";
import {SecreteSummaryType} from "../../types/secret";
import {getSecrets} from "../../api/secret";
import SecretPagination from "../../components/SecretPagination";
import {
    getArticleByKeyword,
    getArticlesByCategoryId,
    getArticlesByPageNumber,
    getUserArticles,
    getUserLikeArticles
} from "../../api/article";
import Categories, {defaultCategory} from "../../components/Categories";
import {ArticleSummaryType, CategoryType} from "../../types/article";
import {getCategories} from "../../api/category";

interface SecretesPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}


export default function SecretesPage({
    handleLogout,
}: SecretesPageProps) {
    // 시크릿 및 페이징 정보
    const [secrets, setSecrets] = useState<SecreteSummaryType[]>([])
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


    const handlePageChange : (event: unknown, value: number) => void = (event, value) => {
        setPage(value) // 바로 page를 사용할 수 없음

        getSecrets(value).then((res) => {
            if (res.status === 200) {
                setSecrets(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
            }
        })
    }


    // 키워드 검색 시 해당 키워드와 관련된 게시글 조회
    const handleKeywordSearch = (keyword : string) => {
        // 검색 키워드로 게시글 조회
        getArticleByKeyword(keyword).then((res) => {
            if (res.status === 200) {
                setSecrets(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
            }

            console.log(res.data.data)
        })
    }

    // 카테고리 변경 시 해당 카테고리와 연관있는 게시글 조회
    const handleCategoryChange = (categoryId: string) => {
        let selectedCategory = categories.find(category => category.categoryId === categoryId);
        if (selectedCategory) {
            setActiveCategory(selectedCategory);

            if (selectedCategory.categoryName === '작성한 글') {
                getUserArticles().then((res) => {
                    if (res.status === 200) {
                        setSecrets(res.data.data.content)
                        setPage(res.data.data.pageNumber)
                        setTotalPages(res.data.data.totalPages)
                    }
                })
            } else if (selectedCategory.categoryName === '좋아요') {
                getUserLikeArticles().then((res) => {
                    if (res.status === 200) {
                        setSecrets(res.data.data.content)
                        setPage(res.data.data.pageNumber)
                        setTotalPages(res.data.data.totalPages)
                    }
                })
            }
            else {
                getArticlesByCategoryId(
                    selectedCategory.categoryId
                ).then((res) => {
                    if (res.status === 200) {
                        setSecrets(res.data.data.content)
                        setPage(res.data.data.pageNumber)
                        setTotalPages(res.data.data.totalPages)
                    }

                    console.log(res.data.data)
                })
            }
        }
    }

    // 컴포넌트 생성시 fetch 처리
    useEffect(() => {
        // 초기화
        setSecrets([])
        setCategories([])

        // 조회처리
        getSecrets().then((res) => {
            if (res.status === 200) {
                setSecrets(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
            }
            console.log(res.data.data)
        })

        getCategories('CT0003').then((res) => {
            if (res.status === 200) {
                setCategories(res.data.data.categories)
            }
        })

    }, [refreshTrigger])

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <SubscriptionCarousel  />
            <Categories
                hasNavigation={true}
                defaultCategoryTap={defaultCategory}
                categories={categories}
                keyword={keyword}
                onChangeCategory={handleCategoryChange}
                onChangeKeyword={setKeyword}
                onSearchKeyword={handleKeywordSearch}
                activeCategory={activeCategory}
            />
            <Secretes
                secrets={secrets}
            />
            <SecretPagination
                page={page}
                totalPages={totalPages}
                handlePageChange={handlePageChange}
            />
            <Footer />
        </>
    )
}