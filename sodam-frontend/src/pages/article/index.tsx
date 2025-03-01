import Header from "../../components/Header";
import ArticleCarousel from "../../components/ArticleCarousel";
import Categories from "../../components/Categories";
import Articles from "../../components/Articles";
import Footer from "../../components/Footer";


export default function ArticlesPage() {
    return (
        <>
            {/* 헤더 */}
            <Header/>

            {/* 베너 */}
            <ArticleCarousel/>

            {/* 카테고리 */}
            <Categories/>

            {/* 콘텐츠 */}
            <Articles/>

            {/* 푸터 */}
            <Footer/>
        </>
    )
}