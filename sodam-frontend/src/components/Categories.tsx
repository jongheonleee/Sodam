import {useEffect, useState} from "react";
import {Category} from "../types/article";

export const defaultCategory : Category = {
    id : "CT001",
    topId : "CT000",
    name : "테스트용 카테고리",
    ord : 1,
    validYN : 0,
};

interface CategoryProps {
    hasNavigation?: boolean;
    defaultCategoryTap?: Category;
}


export default function Categories({
        hasNavigation = true,
        defaultCategoryTap = defaultCategory,
    } : CategoryProps) {

    // 필드값 선언
    const [categories, setCategories] = useState<Category[]>([]);
    const [activeCategory, setActiveCategory] = useState<Category>(defaultCategory);


    // 컴포넌트 생성시 호출(현재 목 api 활용)
    useEffect(() => {
        // categoires 초기화
        setCategories([]);

        // API 호출하여 데이터 가져오기
        fetch("/api/categories")
            .then((res) => res.json())
            .then((data) => {
                if (data?.result === "SUCCESS") {
                    setCategories(data.data);
                }
            })
            .catch((error) => {
                console.log('Error fetching categories', error);
            })
    }, []);

    const handleCategory = (id : string) => {
        // 선택 카테고리 변경
        const activeCategory = categories.find((category) => category.id === id);
        if (activeCategory !== undefined) {
            setActiveCategory(activeCategory);
        }

        // 카테고리 관련 게시글 조회
    }


    return (
        <>
            {/* 카테고리 영역 */}
            {hasNavigation && (
                <div className="article__navigation">
                    <div className="article__category-box">
                        {categories?.map((category) => (
                            <div
                                key={category?.id}
                                role="presentation"
                                onClick={() => handleCategory(category?.id)}
                                className={
                                    activeCategory.id === category.id ? "article__navigation--active" : ""
                                }
                            >
                                { category?.name }
                            </div>
                        ))}
                    </div>

                    {/* 검색 입력 영역 */}
                    <div className="article__search-box">
                        <input
                            type="text"
                            placeholder="검색어를 입력하세요"
                            className="article__search-input"
                            // value={searchTerm}
                            // onChange={(e) => setSearchTerm(e.target.value)}
                        />
                        <button
                            className="article__search-btn"
                            onClick={() => alert("clicked")}
                        >
                            검색
                        </button>
                    </div>
                </div>
            )}
        </>
    )
}