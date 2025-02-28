import {useState} from "react";
import {Link} from "react-router-dom";

export default function SignupForm() {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [chkPassword, setChkPassword] = useState<string>('');
    const [profileImage, setProfileImage] = useState(null);
    const [introduce, setIntroduce] = useState<string>('');
    const [error, setError] = useState<string>('');


    const onSubmit = () => {
        alert("u submitted");
    }

    const onChange = () => {
        alert("u clicked");
    }

    const onImageChange = (e: any) => {
        alert("u clicked uploaing image");
    }

    return (
        <form
            className= "form form--lg"
            onSubmit={onSubmit}
        >
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

            <div className="form__block">
                <label htmlFor="password">비밀번호 확인</label>
                <input
                    type="password"
                    name="chkPassword"
                    id="chkPassword"
                    required
                    onChange={onChange}
                    value={chkPassword}
                />
            </div>

            <div className="form__block">
                <label htmlFor="introduce">자기소개</label>
                <input
                    type="text"
                    name="introduce"
                    id="introduce"
                    required
                    onChange={onChange}
                    value={introduce}
                />
            </div>

            <div className="form__block">
                <label
                    htmlFor="image"
                    className="file-label"
                >
                    프로필 이미지 첨부
                </label>
                <input
                    type="file"
                    name="image"
                    id="image"
                    className="file-input"
                    accept="image/*"
                    onChange={onChange}
                />
                <div className="form__image-upload">
                    {profileImage && (
                        <div className="form__image-preview">
                            <img src={profileImage} alt="프로필 미리보기" />
                            <button type="button" className="form__image-remove" onClick={onImageChange}>
                                ✕
                            </button>
                        </div>
                    )}
                </div>
            </div>


            {error && error?.length > 0 && (
                <div className="form__block">
                    <div className="form__error">{error}</div>
                </div>
            )}

            <div className="form__block">
                계정이 이미 존재하나요?
                <Link to="/login" className="form__link">
                    로그인하기
                </Link>
            </div>

            <div className="form__block">
                <input
                    type="submit"
                    value="회원가입"
                    className="form__btn--submit"
                    disabled={error?.length > 0}
                />
            </div>
        </form>
    )
}