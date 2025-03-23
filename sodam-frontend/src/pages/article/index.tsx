import Header from "../../components/Header";
import ArticleCarousel from "../../components/ArticleCarousel";
import Categories, {defaultCategory} from "../../components/Categories";
import Articles from "../../components/Articles";
import Footer from "../../components/Footer";
import React, {useEffect, useState} from "react";
import {ArticleSummaryType, CategoryType} from "../../types/article";
import {deleteArticle, getArticles, getArticlesByCategoryId} from "../../api/article";
import ArticlePagination from "../../components/ArticlePagination";
import {getCategories} from "../../api/category";

interface ArticlesPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function ArticlesPage({
    handleLogout,
} : ArticlesPageProps) {
    // 게시글 및 페이징 정보
    const [articles, setArticles] = useState<ArticleSummaryType[]>([]);
    const [page, setPage] = useState<number>(1)
    const [totalPages, setTotalPages] = useState<number>(1)

    // 카테고리
    const [categories, setCategories] = useState<CategoryType[]>([]);

    // 검색 키워드
    const [keyword, setKeyword] = useState<string>('');

    // 기본 선정 카테고리
    const [activeCategory, setActiveCategory] = useState<CategoryType>(defaultCategory);


    // 컴포넌트 생성시 카테고리와 게시글 조회
    useEffect(() => {
        // 초기화
        setArticles([]);
        setCategories([]);

        // 게시글 조회
        getArticles().then((res) => {
            if (res.status === 200) {
                setArticles(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
                console.log(res.data.data)
            }
        })

        // 카테고리 조회
        getCategories().then((res) => {
            if (res.status === 200) {
                setCategories(res.data.data.categories)
                console.log(res.data.data)
            }
        })
    }, []);

    // 카테고리 변경 시 해당 카테고리와 연관있는 게시글 조회
    const handleCategoryChange = (categoryId: string) => {
        let selectedCategory = categories.find(category => category.categoryId === categoryId);
        if (selectedCategory) {
            console.log(selectedCategory)
            setActiveCategory(selectedCategory);

            getArticlesByCategoryId(
                selectedCategory.categoryId
            ).then((res) => {
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
        fetch(`/api/articles?category=${activeCategory.categoryId}&keyword=${keyword}`)
            .then((res) => res.json())
            .then((data) => {
                if (data?.result === 'SUCCESS') {
                    setArticles(data.data)
                }
            })
            .catch((error) => console.error('Error fetching articles:', error));
    }

    const handlePageChange : (event: unknown, value: number) => void = (event, value) => {
        setPage(value) // 바로 page를 사용할 수 없음

        getArticles(value).then((res) => {
            if (res.status === 200) {
                setArticles(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
                console.log(res.data.data)
            }
        })
    }

    // 특정 게시글 삭제 처리 핸들링
    const handleArticleDelete = (articleId: number) => {
        if (articleId) {
            deleteArticle(articleId).then((res) => {
                if (res.status === 200) {
                    alert('게시글 삭제됨')
                }
            })
        }
    }

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <ArticleCarousel/>
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
            <Articles
                articles={articles}
                handleArticleDelete={handleArticleDelete}
            />
            <ArticlePagination
                page={page}
                totalPages={totalPages}
                handlePageChange={handlePageChange}
            />
            <Footer/>
        </>
    )
}