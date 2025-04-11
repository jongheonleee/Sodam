import {Link} from "react-router-dom";
import React from "react";

const user = {
    email : "qwefghnm1212@gmail.com"
}

interface HeaderProps {
    handleLogout : (e: React.MouseEvent<HTMLAnchorElement | HTMLButtonElement>) => void
}



export default function Header({
    handleLogout
} : HeaderProps)
{
    return (
        <header className="header">
            <Link to="/" className="header__logo">Sodam 🍃</Link>
            <div>
                <Link to="/articles/new">글쓰기</Link>
                <Link to="/articles">게시글</Link>
                <Link to={`/profile/${user?.email}`}>프로필</Link>
                <Link to="/secrets">구독자 전용</Link>
                <Link to="#" onClick={handleLogout}>로그아웃</Link>
            </div>
        </header>
    )
}