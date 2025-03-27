import './LoginForm2.css'

export default function LoginForm2() {

    return (
        <div className="container">
            <div className="login-container">
                <div className="login-header">
                    <h2>우리들의 성장 이야기 Sodam 🍃</h2>
                    <p>
                        안녕하세요. 함께 성장을 도모하는 Sodam 🍃 커뮤니티입니다. <br/>
                        서비스를 이용하시려면 로그인을 해주세요
                    </p>
                </div>
                <form className="login-form">
                    <input type="email" placeholder="이메일을 입력해 주세요" required/>
                    <div className="password-container">
                        <input type="password" placeholder="비밀번호를 입력해 주세요" required/>
                        <span className="eye-icon">👁️</span>
                    </div>
                    <button type="submit">
                        로그인
                    </button>
                    <button type="submit" className="kakao-login">
                        카카오로 로그인
                    </button>
                </form>
                <div className="login-footer">
                    <p>
                        계정이 없으신가요?
                        <a href="#">회원가입</a>
                    </p>
                </div>
            </div>
        </div>
    )
}