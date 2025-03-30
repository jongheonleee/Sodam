import "./UserUpdateForm.css"
import usePositions from "../../hooks/usePositions";
import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {updateUserInfo} from "../../api/user";
import {Button} from "../core/Button";

export default function UserUpdateForm() {
    const navigate = useNavigate()
    const positions = usePositions()
    const [email, setEmail] = useState<string>('')
    const [name, setName] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [introduce, setIntroduce] = useState<string>('')
    const [positionId, setPositionId] = useState<string>('')
    const [error, setError] = useState<string>('')

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
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

        await updateUserInfo({email, name, password, introduce, positionId})
            .then((res) => {
                if (res.status === 200) {
                    alert('프로필 수정 성공')
                    navigate('/')
                }
            })

    }

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
                <form className="update-form" onSubmit={handleSubmit}>
                    <input type="email" placeholder="변경할 이메일을 입력해주세요" onChange={(e) => setEmail(e.target.value)} value={email} required/>
                    <input type="text" placeholder="변경할 회원 이름을 입력해주세요" onChange={(e) => setName(e.target.value)} value={name} required/>
                    <div className="password-container">
                        <input type="password" placeholder="변경할 비밀번호를 입력해주세요" onChange={(e) => setPassword(e.target.value)} value={password} required/>
                        <span className="eye-icon">👁️</span>
                    </div>
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
                    <textarea placeholder="변경할 자기소개를 입력해주세요." onChange={(e) => setIntroduce(e.target.value)} required>
                    </textarea>
                    <Button
                        type="submit"
                        size="medium"
                    >
                        수정하기
                    </Button>
                </form>
            </div>
        </div>
    )
}