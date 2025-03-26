import React from "react";
import {UserProfileInfoType} from "../types/auth";

interface ProfileProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
    userProfileInfo :UserProfileInfoType | null
}

export default function Profile({
    handleLogout,
    userProfileInfo,
}: ProfileProps) {
    return (
        userProfileInfo ?
            <div className="profile__container">
                {/* 좌측: 프로필 이미지, 이름, 이메일 */}
                <div className="profile__left">
                    <img className="profile__image" src={userProfileInfo.profileImageUrl} alt="프로필 이미지" />
                    <div className="profile__name">{userProfileInfo.name}</div>
                    <div className="profile__email">{userProfileInfo.email}</div>
                    <div className="profile__introduce"> 안녕하세요 저는 AI 풀스택 엔지니어 이종헌입니다.🙋🏻‍♂️ </div>
                </div>

                {/* 우측: 자기소개 및 추가 정보 */}
                <div className="profile__right">

                    <div className="profile__details">
                        {/*{userProfileInfo.introduce && (*/}
                        {/*    <div className="profile__introduce">{userProfileInfo.introduce}</div>*/}
                        {/*)}*/}
                        <div>🧑🏻‍💻 포지션 : AI 서비스 개발자 </div>
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