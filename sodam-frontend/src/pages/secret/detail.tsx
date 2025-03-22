import Header from "../../components/Header";
import Footer from "../../components/Footer";
import React from "react";
import SecretDetail from "../../components/SecretDetail";

interface SecretDetailPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}


export default function SecretDetailPage({
    handleLogout,
} : SecretDetailPageProps) {
    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <SecretDetail />
            <Footer />
        </>
    )
}