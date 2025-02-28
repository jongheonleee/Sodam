const profileImage = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

const onSignOut = () => {}

export default function Profile() {
    return (
        <div className="profile__box">
            <div className="flex__box-lg">
                <img
                    className="profile__image"
                    src={profileImage}
                />
                <div>
                    <div className="profile__email">qwefghnm1212@gmail.com</div>
                    <div className="profile__name">yeonuel</div>
                    <div className="profile__introduce">안녕하세요 풀스택 AI 서비스 개발자를 목표로 학습하고 있는 28살 취준생입니다. 🧑🏻‍💻 </div>
                </div>
            </div>
            <div role="presentation" className="profile__logout" onClick={onSignOut}>
                로그아웃
            </div>
        </div>
    )
}