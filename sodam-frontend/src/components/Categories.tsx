import {useState} from "react";

interface CategoryProps {
    hasNavigation? : boolean;
    defaultCategory? : CategoryType;
}

export type CategoryType = "전체" | "나의 글" | "커리어" | "학습법" | "프로젝트 모집";
export const CATEGORIES: CategoryType[] = [
    "전체",
    "나의 글",
    "커리어",
    "학습법",
    "프로젝트 모집"
]

export default function Categories({
        hasNavigation = true,
        defaultCategory = "전체"
    } : CategoryProps) {

    const [activeCategory, setActiveCategory] = useState<CategoryType>(defaultCategory)
    return (
        <>
            {/* 카테고리 영역 */}
            {hasNavigation && (
                <div className="article__navigation">
                    <div className="article__category-box">
                        {CATEGORIES?.map((category) => (
                            <div
                                key={category}
                                role="presentation"
                                onClick={() => setActiveCategory(category)}
                                className={
                                    activeCategory === category ? "article__navigation--active" : ""
                                }
                            >
                                { category }
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