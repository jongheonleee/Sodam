import Header from "../../components/Header";
import ArticleCarousel from "../../components/ArticleCarousel";
import Categories, {defaultCategory} from "../../components/Categories";
import Articles from "../../components/Articles";
import Footer from "../../components/Footer";


export default function ArticlesPage() {
    return (
        <>
            {/* 헤더 */}
            <Header/>

            {/* 베너 */}
            <ArticleCarousel/>

            <Categories
                hasNavigation={true}
                defaultCategoryTap={defaultCategory}
            />


            {/* 콘텐츠 */}
            <Articles />

            {/* 푸터 */}
            <Footer/>
        </>
    )
}