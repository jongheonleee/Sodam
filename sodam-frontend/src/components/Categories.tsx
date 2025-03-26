import {CategoryType} from "../types/article";

export const defaultCategory : CategoryType = {
    categoryId : "CT001",
    topCategoryId : "CT000",
    categoryName : "테스트용 카테고리",
    isValid : 0,
};

interface CategoryProps {
    hasNavigation?: boolean;
    defaultCategoryTap?: CategoryType;
    categories?: CategoryType[];
    keyword?: string;
    onChangeCategory: (id : string) => void;
    onChangeKeyword : (keyword : string) => void;
    onSearchKeyword : (keyword : string) => void;
    activeCategory : CategoryType;
}


export default function Categories({
        hasNavigation,
        categories,
        keyword,
        onChangeCategory,
        onChangeKeyword,
        onSearchKeyword,
        activeCategory
} : CategoryProps) {

    return (
        <>
            {/* 카테고리 영역 */}
            {hasNavigation && (
                <div className="article__navigation">
                    <div className="article__category-box">
                        {categories?.map((category) => (
                            <div
                                key={category?.categoryId}
                                role="presentation"
                                onClick={() => onChangeCategory(category?.categoryId)}
                                className={
                                    activeCategory.categoryId === category.categoryId ? "article__navigation--active" : ""
                                }
                            >
                                { category?.categoryName }
                            </div>
                        ))}
                    </div>

                    {/* 검색 입력 영역 */}
                    <div className="article__search-box">
                        <input
                            type="text"
                            placeholder="검색어를 입력하세요"
                            className="article__search-input"
                            value={keyword}
                            onChange={(e) => onChangeKeyword(e.target.value)}
                        />
                        <div
                            className="article__search-btn"
                            onClick={() => onSearchKeyword(keyword ? keyword : '')}
                        >
                            검색
                        </div>
                    </div>
                </div>
            )}
        </>
    )
}