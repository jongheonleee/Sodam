import "./UserUpdateForm.css"

export default function UserUpdateForm() {
    return (
        <div className="container">
            <div className="update-container">
                <div className="update-header">
                    <h2>프로필 정보 수정</h2>
                    <p>
                        안녕하세요 <strong>우리들의 성장 이야기 Sodam 🍃</strong>입니다.
                        <br/>
                        프로필 정보 변경을 위해 변경할 프로필 정보를 입력해주세요
                    </p>
                </div>
                <div className="update-form">
                    <input type="email" placeholder="변경할 이메일을 입력해주세요"/>
                    <input type="text" placeholder="변경할 회원 이름을 입력해주세요"/>
                    <div className="password-container">
                        <input type="password" placeholder="변경할 비밀번호를 입력해주세요"/>
                        <span className="eye-icon">👁️</span>
                    </div>
                    <select>
                        <option>프론트엔드 개발자</option>
                        <option>백엔드 개발자</option>
                        <option>AI/ML 개발자</option>
                    </select>
                    <textarea placeholder="변경할 자기소개를 입력해주세요."></textarea>
                    <button type="submit">수정하기</button>
                    <div>
                    </div>
                </div>
            </div>
        </div>
    )
}