import { useState } from "react";
import {Link, useNavigate} from "react-router-dom";
import { SignupProps } from "../types/auth";

export default function SignupForm() {
    // 리다이렉션을 위한 navigate
    const navigate = useNavigate();

    // 회원가입시 필요한 필드값
    const [signupForm, setSignupForm] = useState<SignupProps>({
        email: '',
        name: '',
        password: '',
        checkPassword: '',
        profileImage: null,
        introduce: '',
        profileImageUrl: undefined, // S3 업로드 후 URL 저장
    });

    // 에러 발생시 알림 메시지
    const [error, setError] = useState<string>('');

    // 입력값 변경 핸들러
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSignupForm({
            ...signupForm,
            [e.target.name]: e.target.value
        });

        setError('');
    };

    // 프로필 이미지 업로드 핸들러 (S3에 업로드)
    const handleProfileImageUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        // 업로드된 이미지가 존재하는지 확인
        if (!e.target.files || e.target.files.length === 0) {
            console.log('업로드된 프로필 이미지가 없습니다.');
            return;
        }

        // 업로드된 이미지 조회
        const file = e.target.files[0];

        // 해당 이미지 세터로 폼에 저장
        setSignupForm({
            ...signupForm,
            profileImage: file, // File 객체 저장
        });

        // error 필드 초기화
        setError('');
    };

    // 회원가입 폼 제출 핸들러
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault(); // 기본 폼 제출 방지

        // 필드값 유효성 검증
        // - 이메일 유효성 검증
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailPattern.test(signupForm.email)) {
            setError('유효하지 않은 이메일입니다.');
            return;
        }

        // - 이름 유효성 검증 => (1) 길이 확인(1~20), (2) 특수문자, 공백 확인
        if (!(1 <= signupForm.name.length && signupForm.name.length <= 20)) {
            setError('유효하지 않은 이름입니다. 이름은 최소 1자에서 최대 20자까지 허용합니다.');
            return;
        }

        const namePattern = /^[a-zA-Z0-9가-힣]+$/;
        if (!namePattern.test(signupForm.name)) {
            setError('이름에는 공백이나 특수문자가 포함될 수 없습니다.');
            return;
        }


        // - 비밀번호 유효성 검증 => (1) 길이 확인(8~20), (2) 숫자, 특수문자 포함 여부
        if (!(8 <= signupForm.password.length && signupForm.password.length <= 20)) {
            setError('유효하지 않는 비밀번호입니다. 비밀번호의 길이는 최소 8자에서 최대 20자까지 허용합니다.');
            return;
        }

        const hasNumberOrSpecialChar = /[0-9!@#$%^&*(),.?":{}|<>]/;
        if (!(hasNumberOrSpecialChar.test(signupForm.password))) {
            setError('유효하지 않은 비밀번호입니다. 비밀번호에는 숫자와 특수문자가 포함되어야 합니다.');
            return;
        }

        // - 비밀번호 확인 검증
        if (signupForm.password !== signupForm.checkPassword) {
            setError("비밀번호가 일치하지 않습니다.");
            return;
        }

        // - 자기소개 유효성 검증
        if (!(10 <= signupForm.introduce.length && signupForm.introduce.length <= 1000)) {
            setError('유효하지 않은 자기소개글입니다. 자기소개글은 최소 10자에서 최대 1000자로 작성되야 합니다.');
            return;
        }



        // FormData 생성
        const formData = new FormData();
        formData.append("email", signupForm.email);
        formData.append("name", signupForm.name);
        formData.append("password", signupForm.password);
        formData.append("introduce", signupForm.introduce);

        // 이미지 파일이 있다면 추가
        if (signupForm.profileImage) {
            formData.append("profileImage", signupForm.profileImage);
        }

        // 폼 요청 전송
        try {
            // 목 api 요청 제출
            const response = await fetch("/api/auth/signup", {
                method: "POST",
                body: formData, // FormData 사용
            });

            if (!response.ok) {
                throw new Error("회원가입에 실패했습니다.");
            }

            const data = await response.json();
            console.log(data);

            if (data?.result === 'SUCCESS' && data.data) {
                // 회원가입 성공시 알림
                alert("회원가입이 완료되었습니다.");
                // 홈으로 리다이렉션
                navigate('/', {replace : true})
            }

        } catch (error) {
            console.error("회원가입 오류 : ", error);
            setError("회원가입 오류 : " + error);
        }
    };


    return (
        <form className="form form--lg" onSubmit={handleSubmit}>
            <div className="form__block">
                <label htmlFor="email">이메일</label>
                <input type="email" name="email" id="email" required onChange={handleChange} value={signupForm.email} />
            </div>

            <div className="form__block">
                <label htmlFor="name">이름</label>
                <input type="text" name="name" id="name" required onChange={handleChange} value={signupForm.name} />
            </div>

            <div className="form__block">
                <label htmlFor="password">비밀번호</label>
                <input type="password" name="password" id="password" required onChange={handleChange} value={signupForm.password} />
            </div>

            <div className="form__block">
                <label htmlFor="checkPassword">비밀번호 확인</label>
                <input type="password" name="checkPassword" id="checkPassword" required onChange={handleChange} value={signupForm.checkPassword} />
            </div>

            <div className="form__block">
                <label htmlFor="introduce">자기소개</label>
                <input type="text" name="introduce" id="introduce" required onChange={handleChange} value={signupForm.introduce} />
            </div>

            <div className="form__block">
                <label htmlFor="image" className="file-label">프로필 이미지 첨부</label>
                <input type="file" name="image" id="image" className="file-input" accept="image/*" onChange={handleProfileImageUpload} required />

                {/* 미리보기 (S3 업로드 후 URL이 있으면 사용) */}
                {signupForm.profileImageUrl ? (
                    <div className="form__image-preview">
                        <img src={signupForm.profileImageUrl} alt="프로필 미리보기" />
                        <button type="button" className="form__image-remove" onClick={() => setSignupForm({ ...signupForm, profileImage: null, profileImageUrl: undefined })}>
                            ✕
                        </button>
                    </div>
                ) : (
                    signupForm.profileImage && (
                        <div className="form__image-preview">
                            <img src={URL.createObjectURL(signupForm.profileImage)} alt="프로필 미리보기" />
                        </div>
                    )
                )}
            </div>

            {error && (
                <div className="form__block">
                    <div className="form__error">{error}</div>
                </div>
            )}

            <div className="form__block">
                계정이 이미 존재하나요?
                <Link to="/login" className="form__link">로그인하기</Link>
            </div>

            <div className="form__block">
                <input type="submit" value="회원가입" className="form__btn--submit" disabled={!!error} />
            </div>
        </form>
    );
}
