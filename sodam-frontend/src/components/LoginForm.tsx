import {useState} from "react";
import {Link} from "react-router-dom";

export default function LoginForm() {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>('');

    const onChange = () => {
        alert("u clicked");
    }

    const onSubmit = () => {
        alert("u submitted");
    }

    const handleKakaoLogin = () => {
        alert("u clicked kakao login");
    }

    return (
        <form
            className="form form--lg"
            onSubmit={onSubmit}
        >
            {/* 로그인 영역 */}
            <div className="form__block">
                <label htmlFor="email">이메일</label>
                <input
                    type="email"
                    name="email"
                    id="email"
                    required
                    onChange={onChange}
                    value={email}
                />
            </div>

            <div className="form__block">
                <label htmlFor="password">비밀번호</label>
                <input
                    type="password"
                    name="password"
                    id="password"
                    required
                    onChange={onChange}
                    value={password}
                />
            </div>

            {/* 에러 표시 영역 */}
            {error && error?.length > 0 && (
                <div className="form__block">
                    <div className="form__error">{error}</div>
                </div>
            )}


            {/* 회원가입 페이지 이동 영역 */}
            <div className="form__block">
                계정이 없으신가요?
                <Link to="/signup" className="form__link">
                    회원가입하기
                </Link>
            </div>

            {/* 로그인, 카카오 로그인 버튼 영역 */}
            <div className="form__block">
                <input
                    type="submit"
                    value="로그인"
                    className="form__btn--submit"
                    disabled={error?.length > 0}
                />
            </div>

            {/* 카카오 로그인 버튼 */}
            <div className="form__block">
                <button
                    className="kakao-login-btn"
                    onClick={handleKakaoLogin}
                >
                    <img src="https://upload.wikimedia.org/wikipedia/commons/e/e3/KakaoTalk_logo.svg" alt="카카오 아이콘"/>
                    카카오톡으로 로그인
                </button>
            </div>
        </form>
    );
}