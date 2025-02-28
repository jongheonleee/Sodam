import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Subscriptions from "../../components/Subscriptions";
import SubscriptionCarousel from "../../components/SubscriptionCarousel";

export default function SubscriptionPage() {
    return (
        <>
            <Header />
            <SubscriptionCarousel />
            <Subscriptions/>
            <Footer />
        </>
    )
}