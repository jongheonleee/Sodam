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
    categories?: Category[];
    keyword?: string;
    onChangeCategory: (id : string) => void;
    onChangeKeyword : (keyword : string) => void;
    onSearchKeyword : (keyword : string) => void;
    activeCategory : Category;
}


export default function Categories({
        hasNavigation,
        defaultCategoryTap,
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
                                key={category?.id}
                                role="presentation"
                                onClick={() => onChangeCategory(category?.id)}
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