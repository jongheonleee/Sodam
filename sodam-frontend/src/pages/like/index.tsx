import Footer from "../../components/Footer";
import Header from "../../components/Header";
import ArticleLikes from "../../components/ArticleLikes";
import React from "react";

interface LikePageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function LikePage({
    handleLogout
} : LikePageProps) {

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <ArticleLikes/>
            <Footer />
        </>
    )
}