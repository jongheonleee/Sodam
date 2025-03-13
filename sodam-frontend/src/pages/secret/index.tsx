import Header from "../../components/Header";
import Footer from "../../components/Footer";
import SubscriptionCarousel from "../../components/SubscriptionCarousel";
import Secretes from "../../components/Secretes";
import React from "react";

interface SecretesPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}


export default function SecretesPage({
    handleLogout,
}: SecretesPageProps) {
    return (
        <>
            {/* 헤더 */}
            <Header
                handleLogout={handleLogout}
            />

            {/* 베너 */}
            <SubscriptionCarousel  />

            {/* 콘텐츠 */}
            <Secretes />

            {/* 푸터 */}
            <Footer />
        </>
    )
}