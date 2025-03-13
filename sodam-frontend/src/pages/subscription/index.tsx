import Header from "../../components/Header";
import Footer from "../../components/Footer";
import Subscriptions from "../../components/Subscriptions";
import SubscriptionCarousel from "../../components/SubscriptionCarousel";
import React from "react";

interface SubscriptionPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function SubscriptionPage({
    handleLogout
}: SubscriptionPageProps) {
    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <SubscriptionCarousel />
            <Subscriptions/>
            <Footer />
        </>
    )
}