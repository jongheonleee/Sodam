import Header from "../../components/Header";
import Footer from "../../components/Footer";
import SubscriptionCarousel from "../../components/SubscriptionCarousel";
import Secretes from "../../components/Secretes";

export default function SecretesPage() {
    return (
        <>
            {/* 헤더 */}
            <Header />

            {/* 베너 */}
            <SubscriptionCarousel  />

            {/* 콘텐츠 */}
            <Secretes />

            {/* 푸터 */}
            <Footer />
        </>
    )
}