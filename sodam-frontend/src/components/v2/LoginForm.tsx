import './LoginForm.css'
import React from "react";
import {Link} from "react-router-dom";
import {Button} from "../core/Button";
import {ButtonGroup} from "../core/ButtonGroup";

interface LoginFormProps {
    email : string;
    password : string;
    error: string;
    onChangeEmail : (e: React.ChangeEvent<HTMLInputElement>) => void;
    onChangePassword : (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSubmit : (event: React.FormEvent<HTMLFormElement>) => void;
    onKakaoLogin : () => void;
}


export default function LoginForm({
    email,
    password,
    onChangeEmail,
    onChangePassword,
    onSubmit,
    onKakaoLogin,
    error
}: LoginFormProps) {

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
                <form className="login-form" onSubmit={onSubmit}>
                    <input type="email" placeholder="이메일을 입력해 주세요" value={email} onChange={(e) => {onChangeEmail(e)}} required/>
                    <div className="password-container">
                        <input type="password" placeholder="비밀번호를 입력해 주세요" value={password} onChange={(e) => onChangePassword(e)} required/>
                        <span className="eye-icon">👁️</span>
                    </div>
                    <ButtonGroup>
                        <Button
                            type="submit"
                            size="medium"
                            isDisabled={error?.length > 0}
                        >
                            로그인
                        </Button>
                        <Button
                            type="submit"
                            size="medium"
                            className="kakao-login"
                            onClick={onKakaoLogin}
                            isDisabled={error?.length > 0}
                        >
                            카카오로 로그인
                        </Button>
                    </ButtonGroup>
                </form>
                <div className="login-footer">
                    <p>
                        계정이 없으신가요?
                        <Link to="/signup">
                             회원가입하기
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    )
}