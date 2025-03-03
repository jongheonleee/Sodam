import {Link} from "react-router-dom";

const user = {
    email : "qwefghnm1212@gmail.com"
}


export default function Header() {
    return (
        <header className="header">
            <Link to="/" className="header__logo">Sodam 🍃</Link>
            <div>
                <Link to="/articles/new">글쓰기</Link>
                <Link to="/articles">게시글</Link>
                <Link to={`/profile/${user?.email}`}>프로필</Link>
                <Link to={`/articles/like/${user?.email}`}>좋아요</Link>
                <Link to="/secretes">구독자 전용</Link>
            </div>
        </header>
    )
}