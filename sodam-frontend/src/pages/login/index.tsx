import LoginForm from "../../components/LoginForm";
import { useState } from "react";
import {Link, useNavigate} from "react-router-dom";
import {login} from "../../api/login";

interface LoginProps {
    setIsAuthenticated: (isAuthenticated: boolean) => void;
}

export default function Login({
    setIsAuthenticated,
}: LoginProps) {
    const navigate = useNavigate();
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>('');

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) =>  {
        setEmail(e.target.value);
        if (error.length > 0) {
            setError('');
        }
    }

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
        if (error.length > 0) {
            setError('');
        }
    }


    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        // 에러 메시지 초기화
        setError('');

        // 필드값 유효성 검증
        // - 이메일 유효성 검증
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailPattern.test(email)) {
            setError('유효하지 않은 이메일입니다.');
            return;
        }


        // - 비밀번호 유효성 검증 => (1) 길이 확인(8~20), (2) 숫자, 특수문자 포함 여부
        if (!(8 <= password.length && password.length <= 20)) {
            setError('유효하지 않는 비밀번호입니다. 비밀번호의 길이는 최소 8자에서 최대 20자까지 허용합니다.');
            return;
        }

        const hasNumberOrSpecialChar = /[0-9!@#$%^&*(),.?":{}|<>]/;
        if (!(hasNumberOrSpecialChar.test(password))) {
            setError('유효하지 않은 비밀번호입니다. 비밀번호에는 숫자와 특수문자가 포함되어야 합니다.');
            return;
        }

        login({ email, password})
            .then((res) => {
                if (res.status === 200) {
                    // 로그인 성공하면 로컬 스토리지에 백엔드에서 발급된 토큰을 저장함
                    localStorage.setItem('token', res.data.data)
                    // 잘 발급되었는지 확인해보기
                    console.log(localStorage.getItem('token'))
                    // 인증 여부 확인 및 홈으로 리다이렉션
                    setIsAuthenticated(true)
                    navigate('/')
                }
            })
            .catch(error => {
                console.error('[Error] 에러 발생 : ' + error);
            })

    }

    const KAKAO_CLIENT_ID = "카카오 REST API 키";
    const REDIRECT_URI = "http://localhost:3000/auth/kakao/callback";

    const handleKakaoLogin = () => {
        const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;
        window.location.href = kakaoAuthUrl;
    };



    return (
        <>
            <header className="header">
                <Link to="/" className="header__logo">Sodam 🍃</Link>
                <div>
                    <Link to="/signup">회원가입</Link>
                    <Link to="/login">로그인</Link>
                </div>
            </header>
            <LoginForm
                email={email}
                password={password}
                onSubmit={handleSubmit}
                onChangeEmail={handleEmailChange}
                onChangePassword={handlePasswordChange}
                onKakaoLogin={handleKakaoLogin}
                error={error}
            />
        </>
    )
}