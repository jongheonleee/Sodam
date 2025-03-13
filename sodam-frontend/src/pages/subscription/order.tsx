import Header from "../../components/Header";
import SubscriptionCarousel from "../../components/SubscriptionCarousel";
import Subscriptions from "../../components/Subscriptions";
import Footer from "../../components/Footer";
import SubscriptionOrderForm from "../../components/SubscriptionOrderForm";
import React from "react";

interface SubscriptionOrderPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}


export default function SubscriptionOrderPage({
    handleLogout,
}: SubscriptionOrderPageProps) {
    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <SubscriptionOrderForm />
            <Footer />
        </>
    )
}