import React from "react";
import {UserProfileInfoType} from "../types/auth";
import {useNavigate} from "react-router-dom";

interface ProfileProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
    userProfileInfo :UserProfileInfoType | null
}

export default function Profile({
    handleLogout,
    userProfileInfo,
}: ProfileProps) {
    const navigate = useNavigate()
    return (
        userProfileInfo ?
            <div className="profile__container">
                {/* 좌측: 프로필 이미지, 이름, 이메일 */}
                <div className="profile__left">
                    <img className="profile__image" src={userProfileInfo.profileImageUrl} alt="프로필 이미지" />
                    <div className="profile__name">{userProfileInfo.name}</div>
                    <div className="profile__email">{userProfileInfo.email}</div>
                    <div className="profile__introduce"> {userProfileInfo.introduce} </div>
                    <div className="profile__edit" onClick={() => navigate("/edit-profile")}>프로필 수정</div>
                </div>

                {/* 우측: 자기소개 및 추가 정보 */}
                <div className="profile__right">

                    <div className="profile__details">
                        <div>🧑🏻‍💻 포지션: {userProfileInfo.positions.map(position => position)} </div>
                        <div>🏆 랭킹: {userProfileInfo.ranking}</div>
                        <div>🔰 회원 등급: {userProfileInfo.grade}</div>
                        <div>📝 작성한 게시글: {userProfileInfo.articleTotalCnt}</div>
                        <div>📜 보유 구독권: {userProfileInfo.subscription}</div>
                    </div>

                </div>
            </div>

            : <div> 유저의 프로필이 없습니다. </div>
    )
}