import {Navigate, Route, Routes} from "react-router-dom";
import Home from "../pages/home";
import React from "react";
import Login from "../pages/login";
import Signup from "../pages/signup";
import ProfilePage from "../pages/profile";
import Article from "../pages/article/new";
import ArticleDetailPage from "../pages/article/detail";
import LikePage from "../pages/like";
import SubscriptionPage from "../pages/subscription";
import SubscriptionOrderPage from "../pages/subscription/order";
import SecretesPage from "../pages/secret";
import SecretDetailPage from "../pages/secret/defail";
import ArticlesPage from "../pages/article";
import ArticleEditPage from "../pages/article/edit";

export default function Router() {
    // 추후에 사용자 인증 여부에 따라서 라우팅 처리 달리 만들기
    return (
        <>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/articles" element={<ArticlesPage />} />
                <Route path="/articles/:id" element={<ArticleDetailPage />} />
                <Route path="/articles/new" element={<Article />} />
                <Route path="/articles/edit/:id" element={<ArticleEditPage />} />
                <Route path="/articles/like/:email" element={<LikePage />} />
                <Route path="/profile/:email" element={<ProfilePage />} />
                <Route path="/subscription" element={<SubscriptionPage />} />
                <Route path="/secretes" element={<SecretesPage />} />
                <Route path="/secretes/:id" element={<SecretDetailPage />} />
                <Route path="/subscription/order/:id" element={<SubscriptionOrderPage />} />
                <Route path="*" element={<Navigate replace to="/" />} />
            </Routes>
        </>
    )
}