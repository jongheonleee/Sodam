import ArticleCarousel from "../../components/ArticleCarousel";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Articles from "../../components/Articles";
import Categories, {defaultCategory} from "../../components/Categories";
import React, {useEffect, useState} from "react";
import {ArticleSummaryType, CategoryType} from "../../types/article";
import {getArticlesByTag} from "../../api/article";

interface HomeProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function Home({
    handleLogout,
} : HomeProps) {
    // 게시글 및 페이징 정보
    const [articles, setArticles] = useState<ArticleSummaryType[]>([])
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

        // 카테고리와 게시글 조회
        Promise.all([
            fetch('/api/articles').then(res => res.json()),
            fetch('/api/categories').then(res => res.json()),
        ])
            .then(([articlesData, categoriesData]) => {
                if (articlesData?.result === 'SUCCESS') {
                    setArticles(articlesData.data);
                }

                if (categoriesData?.result === 'SUCCESS') {
                    setCategories(categoriesData.data);
                }
            })
            .catch(error => {
                console.error('Error fetching data', error);
            });
    }, []);

    // 카테고리 변경 시 해당 카테고리와 연관있는 게시글 조회
    const handleCategoryChange = (id: string) => {
        const selectedCategory = categories.find((category) => id === category.categoryId);
        if (selectedCategory) {
            setActiveCategory(selectedCategory);
        }

        // 선택된 카테고리에 맞는 게시글 조회
        fetch(`/api/articles?category=${activeCategory.categoryId}`)
            .then((res) => res.json())
            .then((data) => {
                if (data?.result === 'SUCCESS') {
                    setArticles(data.data);
                }
            })
            .catch((error) => console.error('Error fetching articles:', error));
    }

    // 키워드 검색 시 해당 키워드와 관련된 게시글 조회
    const handleSearchKeyword = (keyword: string) => {
        // 검색 키워드로 게시글 조회
        fetch(`/api/articles?category=${activeCategory.categoryId}&keyword=${keyword}`)
            .then((res) => res.json())
            .then((data) => {
                if (data?.result === 'SUCCESS') {
                    setArticles(data.data)
                }
            })
            .catch((error) => {
                console.error('Error fetching articles:', error)
            });
    }

    const handleKeywordChange = (keyword : string) => {
        setKeyword(keyword);
    }

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




    return (
        <>
            {/* 헤더 */}
            <Header
                handleLogout={handleLogout}
            />

            {/* 베너 */}
            <ArticleCarousel />

            <Categories
                hasNavigation={true}
                defaultCategoryTap={defaultCategory}
                categories={categories}
                keyword={keyword}
                onChangeCategory={handleCategoryChange}
                onChangeKeyword={handleKeywordChange}
                onSearchKeyword={handleSearchKeyword}
                activeCategory={activeCategory}
            />

            {/* 콘텐츠 */}
            <Articles
                articles={articles}
                handleArticleDelete={handleArticleDelete}
                handleTagSearch={handleTagSearch}
            />

            {/* 푸터 */}
            <Footer/>
        </>
    )
}