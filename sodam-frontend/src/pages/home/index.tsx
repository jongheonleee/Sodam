import ArticleCarousel from "../../components/ArticleCarousel";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Articles from "../../components/Articles";
import Categories, {defaultCategory} from "../../components/Categories";


export default function Home() {
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