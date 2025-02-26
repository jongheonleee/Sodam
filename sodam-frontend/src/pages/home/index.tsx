import Carousel from "../../components/Carousel";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Articles from "../../components/Articles";
import Categories from "../../components/Categories";


export default function Home() {
    return (
        <>
            {/* 헤더 */}
            <Header />

            {/* 베너 */}
            <Carousel  />

            {/* 카테고리 */}
            <Categories />

            {/* 콘텐츠 */}
            <Articles />

            {/* 푸터 */}
            <Footer />
        </>
    )
}