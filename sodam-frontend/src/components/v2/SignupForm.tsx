import "./SignupForm.css"
import {Link, useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {signup} from "../../api/signup";
import {Button} from "../core/Button";
import usePositions from "../../hooks/usePositions";

export default function SignupForm() {
    // 리다이렉션을 위한 navigate
    const navigate = useNavigate();

    // 회원가입시 필요한 필드값 -- 이 부분 form으로 보내지 말기
    const positions = usePositions()
    const [email, setEmail] = useState<string>('')
    const [name, setName] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [introduce, setIntroduce] = useState<string>('')
    const [positionId, setPositionId] = useState<string>('')

    // 에러 발생시 알림 메시지
    const [error, setError] = useState<string>('')

    // 회원가입 폼 제출 핸들러
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault() // 기본 폼 제출 방지

        // 필드값 유효성 검증
        // - 이메일 유효성 검증
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        if (!emailPattern.test(email)) {
            setError('유효하지 않은 이메일입니다.')
            return;
        }

        // - 이름 유효성 검증 => (1) 길이 확인(1~20), (2) 특수문자, 공백 확인
        if (!(1 <= name.length && name.length <= 20)) {
            setError('유효하지 않은 이름입니다. 이름은 최소 1자에서 최대 20자까지 허용합니다.')
            return;
        }

        const namePattern = /^[a-zA-Z0-9가-힣]+$/;
        if (!namePattern.test(name)) {
            setError('이름에는 공백이나 특수문자가 포함될 수 없습니다.')
            return;
        }


        // - 비밀번호 유효성 검증 => (1) 길이 확인(8~20), (2) 숫자, 특수문자 포함 여부
        if (!(8 <= password.length && password.length <= 20)) {
            setError('유효하지 않는 비밀번호입니다. 비밀번호의 길이는 최소 8자에서 최대 20자까지 허용합니다.')
            return;
        }

        const hasNumberOrSpecialChar = /[0-9!@#$%^&*(),.?":{}|<>]/;
        if (!(hasNumberOrSpecialChar.test(password))) {
            setError('유효하지 않은 비밀번호입니다. 비밀번호에는 숫자와 특수문자가 포함되어야 합니다.')
            return;
        }


        // - 자기소개 유효성 검증
        if (!(10 <= introduce.length && introduce.length <= 1000)) {
            setError('유효하지 않은 자기소개글입니다. 자기소개글은 최소 10자에서 최대 1000자로 작성되야 합니다.')
            return;
        }



        signup({email, name, password, introduce, positionId})
            .then((res) => {
                if (res.status === 200) {
                    alert('회원가입 성공')
                    navigate('/')}
            })
            .catch(error => {
                console.error('[Error] 에러 발생 : ' + error)
            })
    }

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
                <form className="join-form" onSubmit={handleSubmit}>
                    <input type="email" placeholder="이메일을 입력해주세요" onChange={(e) => setEmail(e.target.value)} value={email} required/>
                    <input type="text" placeholder="이름을 입력해주세요" onChange={(e) => setName(e.target.value)} value={name} required/>
                    <div className="password-container">
                        <input type="password" placeholder="비밀번호를 입력해 주세요" onChange={(e) => setPassword(e.target.value)} value={password} required/>
                        <span className="eye-icon">👁️</span>
                    </div>
                    {/* 포지션 내용 보여주기 - 추후에 select, option 도 컴포넌트로 정의해서 관리(components - core) */}
                    { positions && positions.length > 0
                        ? <select onChange={(e) => setPositionId(e.target.value)}>
                            {positions.map((position) => (
                                <option key={position.positionId} value={position.positionId}>
                                    {position.positionName}
                                </option>
                            ))}
                          </select>
                        : <div>
                            조회된 포지션 내용이 없습니다.
                          </div>
                    }
                    <textarea placeholder="자기소개를 입력해주세요." onChange={(e) => setIntroduce(e.target.value)} required>
                    </textarea>
                    <div className="join-remember">
                        <label>
                            <input type="checkbox"/>
                            가입 후 자동 로그인
                        </label>
                        <Link to="#">
                            회원 약관 보기
                        </Link>
                    </div>
                    <Button
                        type="submit"
                        size="medium"
                    >
                        회원 가입
                    </Button>
                </form>
                <div className="join-footer">
                    <p>
                        계정이 있으신가요?
                        <Link to="/login" >로그인하기</Link>
                    </p>
                </div>
            </div>
        </div>
    )
}