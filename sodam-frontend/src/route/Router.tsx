import {Navigate, Route, Routes} from "react-router-dom";
import Home from "../pages/home";
import React from "react";
import Login from "../pages/login";
import Signup from "../pages/signup";
import ProfilePage from "../pages/profile";
import ArticleNewPage from "../pages/article/new";
import ArticleDetailPage from "../pages/article/detail";
import LikePage from "../pages/like";
import SubscriptionPage from "../pages/subscription";
import SubscriptionOrderPage from "../pages/subscription/order";
import SecretesPage from "../pages/secret";
import SecretDetailPage from "../pages/secret/detail";
import ArticlesPage from "../pages/article";
import ArticleEditPage from "../pages/article/edit";
import KakaoAuthRedirect from "../pages/login/kakaoAuthRedirect";

interface RouterProps {
    isAuthenticated : boolean,
    setIsAuthenticated: (isAuthenticated: boolean) => void;
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function Router({
    isAuthenticated,
    setIsAuthenticated,
    handleLogout,
} : RouterProps) {
    // 추후에 사용자 인증 여부에 따라서 라우팅 처리 달리 만들기
    return (
        <>
            <Routes>
            {/* 인증된 유저와 사용자는 서로 다른 라우팅을 제공 */}
                { isAuthenticated ? (
                    <>
                        <Route path="/" element={<Home handleLogout={handleLogout} />} />
                        <Route path="/articles" element={<ArticlesPage handleLogout={handleLogout}  />} />
                        <Route path="/articles/:articleId" element={<ArticleDetailPage handleLogout={handleLogout}  />} />
                        <Route path="/articles/new" element={<ArticleNewPage handleLogout={handleLogout}  />} />
                        <Route path="/articles/edit/:articleId" element={<ArticleEditPage handleLogout={handleLogout} />} />
                        <Route path="/articles/like/:email" element={<LikePage handleLogout={handleLogout}  />} />
                        <Route path="/profile/:email" element={<ProfilePage handleLogout={handleLogout} />} />
                        <Route path="/subscription" element={<SubscriptionPage handleLogout={handleLogout}  />} />
                        <Route path="/secretes" element={<SecretesPage handleLogout={handleLogout}  />} />
                        <Route path="/secretes/:id" element={<SecretDetailPage handleLogout={handleLogout}  />} />
                        <Route path="/subscription/order/:id" element={<SubscriptionOrderPage handleLogout={handleLogout} />} />
                        <Route path="*" element={<Navigate replace to="/" />} />
                    </>
                ) : (
                    <>
                        <Route path="/login" element={<Login setIsAuthenticated={setIsAuthenticated}/>}></Route>
                        <Route path="/signup" element={<Signup />}></Route>
                        <Route path="/login/oauth2/code/kakao" element={<KakaoAuthRedirect />}></Route>
                        <Route path="*" element={<Login setIsAuthenticated={setIsAuthenticated}/>}></Route>
                    </>
                )}
            </Routes>
        </>
    )
}