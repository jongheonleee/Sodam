import "./SignupForm2.css"

export default function SignupForm2() {


    return (
        <div className="container">
            <div className="join-container">
                <div className="join-header">
                    <h2>환영합니다!</h2>
                    <p>
                        안녕하세요 <strong>우리들의 성장 이야기 Sodam 🍃</strong>입니다.
                        <br/>
                        회원가입을 위해 몇 가지 정보만 입력해 주시면 Sodam 🍃 커뮤니티를 이용할 수 있습니다.
                    </p>
                </div>
                <form className="join-form" action="http://sodam.com/...">
                    <input type="email" placeholder="이메일을 입력해 주세요" required/>
                    <div className="password-container">
                        <input type="password" placeholder="비밀번호를 입력해 주세요" required/>
                        <span className="eye-icon">👁️</span>
                    </div>
                    <select>
                        <option value="1">프론트엔드 개발자</option>
                        <option value="2">백엔드 개발자</option>
                        <option value="3">풀스택 개발자</option>
                        <option value="4">AI/ML 개발자</option>
                        <option value="5">디자이너</option>
                        <option value="6">학생</option>
                        <option value="7">취업 준비생</option>
                    </select>
                    <textarea placeholder="자기소개를 입력해주세요.">
            </textarea>
                    <div className="join-remember">
                        <label>
                            <input type="checkbox"/>
                            가입 후 자동 로그인
                        </label>
                        <a href="#">
                            회원 약관 보기
                        </a>
                    </div>
                    <button type="submit">
                        회원 가입
                    </button>
                </form>
                <div className="join-footer">
                    <p>
                        계정이 있으신가요?
                        <a href="#">로그인</a>
                    </p>
                </div>
            </div>
        </div>
    )
}