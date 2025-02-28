import {Navigate, Route, Routes} from "react-router-dom";
import Home from "../pages/home";
import React from "react";
import Login from "../pages/login";
import Signup from "../pages/signup";
import ProfilePage from "../pages/profile";
import Article from "../pages/article/new";
import ArticleDetail from "./ArticleDetail";
import ArticleDetailPage from "../pages/article/detail";

export default function Router() {
    // 추후에 사용자 인증 여부에 따라서 라우팅 처리 달리 만들기
    return (
        <>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/articles" element={<h1>Articles</h1>} />
                <Route path="/articles/:id" element={<ArticleDetailPage />} />
                <Route path="/articles/new" element={<Article />} />
                <Route path="/articles/edit/:id" element={<h1>Articles Edit Page</h1>} />
                <Route path="/articles/like" element={<h1>Articles Like</h1>} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/subscription" element={<h1>Subscription Page</h1>} />
                <Route path="/secretes" element={<h1>Subscription Articles[secretes]</h1>} />
                <Route path="/subscription" element={<h1>Subscription Page</h1>} />
                <Route path="*" element={<Navigate replace to="/" />} />
            </Routes>
        </>
    )
}