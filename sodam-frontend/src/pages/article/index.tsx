import Header from "../../components/Header";
import ArticleCarousel from "../../components/ArticleCarousel";
import Categories, {defaultCategory} from "../../components/Categories";
import Articles from "../../components/Articles";
import Footer from "../../components/Footer";
import {useEffect, useState} from "react";
import {ArticleSummary, Category} from "../../types/article";


export default function ArticlesPage() {
    // 게시글
    const [articles, setArticles] = useState<ArticleSummary[]>([]);

    // 카테고리
    const [categories, setCategories] = useState<Category[]>([]);

    // 검색 키워드
    const [keyword, setKeyword] = useState<string>('');

    // 기본 선정 카테고리
    const [activeCategory, setActiveCategory] = useState<Category>(defaultCategory);


    // 컴포넌트 생성시 카테고리와 게시글 조회
    useEffect(() => {
        // 초기화
        setArticles([]);
        setCategories([]);

        // 필요한 데이터 조회
        Promise.all([
            fetch('/api/articles').then(res => {
                console.log("[articles]Response Content-Type:", res.headers.get("content-type"));
                return res.json();
            }),
            fetch('/api/categories').then(res => {
                console.log("[categories]Response Content-Type:", res.headers.get("content-type"));
                return res.json();
            })
        ])
            .then(([articlesData, categoriesData]) => {
                if (articlesData?.result === 'SUCCESS') {
                    setArticles(articlesData.data);
                }

                if (categoriesData?.result !== 'SUCCESS') {
                    setCategories(categoriesData.data);
                }
            })
            .catch(error => {
                console.log('Error fetching data', error);
            });
    }, []);

    // 카테고리 변경 시 해당 카테고리와 연관있는 게시글 조회
    const handleCategoryChange = (id: string) => {
        const selectedCategory = categories.find((category) => id === category.id);
        if (selectedCategory) {
            setActiveCategory(selectedCategory);
        }

        // 선택된 카테고리에 맞는 게시글 조회
        fetch(`/api/articles?category=${activeCategory.id}`)
            .then((res) => res.json())
            .then((data) => {
                if (data?.result === 'SUCCESS') {
                    setArticles(data.data);
                }
            })
            .catch((error) => console.error('Error fetching articles:', error));
    }


    // 키워드 검색 시 해당 키워드와 관련된 게시글 조회
    const handleKeywordSearch = (keyword : string) => {
        // 검색 키워드로 게시글 조회
        fetch(`/api/articles?category=${activeCategory.id}&keyword=${keyword}`)
            .then((res) => res.json())
            .then((data) => {
                if (data?.result === 'SUCCESS') {
                    setArticles(data.data)
                }
            })
            .catch((error) => console.error('Error fetching articles:', error));
    }

    return (
        <>
            {/* 헤더 */}
            <Header/>

            {/* 베너 */}
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

            {/* 콘텐츠 */}
            <Articles
                articles={articles}
            />

            {/* 푸터 */}
            <Footer/>
        </>
    )
}