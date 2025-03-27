import {Link} from "react-router-dom";
import SignupForm from "../../components/SignupForm";

export default function Signup() {
    return (
        <>
            {/* 헤더 영역 */}
            <header className="header">
                <Link to="/" className="header__logo">Sodam 🍃</Link>
                <div>
                    <Link to="/signup">회원가입</Link>
                    <Link to="/login">로그인</Link>
                </div>
            </header>

            {/* 회원가입 영역 */}
            <SignupForm />
        </>
    )
}