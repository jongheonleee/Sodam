import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import axios from "axios";

interface KakaoAuthRedirectProps {
    setIsAuthenticated: (isAuthenticated: boolean) => void;
}

export default function KakaoAuthRedirect({
    setIsAuthenticated,
}: KakaoAuthRedirectProps) {
    const navigate = useNavigate();

    useEffect(() => {
        // 현재 URL에서 카카오 인증 후 리턴된 authorization code 추출
        const code = new URL(window.location.href).searchParams.get("code")
        console.log(code)

        // 백엔드에 인증 코드 전송하여 JWT 토큰 받기
        if (code) {
            axios.post('https://localhost:8443/api/v1/auth/callback', { code })
                .then(res => {
                    // 로그인 성공하면 로컬 스토리지에 백엔드에서 발급된 토큰을 저장함
                    localStorage.setItem('token', res.data.data.accessToken)
                    localStorage.setItem('refresh_token', res.data.data.refreshToken)
                    // 인증 여부 확인 및 홈으로 리다이렉션
                    setIsAuthenticated(true)
                    navigate('/')
                })
                .catch(error => {
                    console.error('카카오 로그인 실패:', error)
                });
        }
    }, );

    return (
        <div>
            카카오 로그인 처리중...
        </div>
    )

}