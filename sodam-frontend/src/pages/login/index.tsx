import LoginForm from "../../components/LoginForm";
import Header from "../../components/Header";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {
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

        // 입력값 유효성 검증
        // - 이메일 4자 이상
        if (!(email.length > 4)) {
            setError('잘못된 형식의 이메일 입력');
            return
        }

        // - 비밀번호 8자 이상
        if (!(password.length >= 8)) {
            setError('잘못된 형식의 비밀번호 입력');
            return;
        }

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            const data = await response.json();
            console.log(data);

            if (data?.result === 'SUCCESS' && data.data) {
                // 메인 페이지로 이동
                navigate('/', {replace : true})
            }

            console.log(data?.data);

        } catch (error) {
            console.error('로그인 오류:', error);
            setError('로그인 오류: ' + error)
        }

    }

    const KAKAO_CLIENT_ID = "카카오 REST API 키";
    const REDIRECT_URI = "http://localhost:3000/auth/kakao/callback";

    const handleKakaoLogin = () => {
        const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;
        window.location.href = kakaoAuthUrl;
    };



    return (
        <>
            <Header />
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