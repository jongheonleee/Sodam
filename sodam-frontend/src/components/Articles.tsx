const articles = [
    {
        id: 1,
        email: "qwefghnm1212@gmail.com",
        author: "qwefghnm1212",
        createdAt: "2025. 02. 25 오후 03:35:01",
        userImage: "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "테스트 게시글 제목",
        summary: "테스트 게시글 요약글",
        tags : [
            "태그1",
            "태그2",
            "태그3"
        ],
    },
    {
        id: 2,
        email: "qwefghnm1212@gmail.com",
        author: "qwefghnm1212",
        createdAt: "2025. 02. 25 오후 03:35:01",
        userImage: "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "테스트 게시글 제목",
        summary: "테스트 게시글 요약글",
        tags : [
            "태그1",
        ],
    },
    {
        id: 3,
        email: "qwefghnm1212@gmail.com",
        author: "qwefghnm1212",
        createdAt: "2025. 02. 25 오후 03:35:01",
        userImage: "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "테스트 게시글 제목",
        summary: "테스트 게시글 요약글",
        tags : [
        ],
    },
    {
        id: 4,
        email: "qwefghnm1212@gmail.com",
        author: "qwefghnm1212",
        createdAt: "2025. 02. 25 오후 03:35:01",
        userImage: "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "테스트 게시글 제목",
        summary: "테스트 게시글 요약글",
        tags : [
            "태그1",
            "태그2",
            "태그3"
        ],
    },
    {
        id: 5,
        email: "qwefghnm1212@gmail.com",
        author: "qwefghnm1212",
        createdAt: "2025. 02. 25 오후 03:35:01",
        userImage: "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        title: "테스트 게시글 제목",
        summary: "테스트 게시글 요약글",
        tags : [
            "태그1",
            "태그2",
        ],
    }
]


export default function Articles() {
    return (
        <>
            <div className="article__list">
                {// 아티클이 존재하면 이터레이터 처리하여 그려주기, 그게아니면 게시글 없을을 보여주기
                 articles?.length > 0 ? (
                     articles?.map((article) => (
                         // 각 아티클 단위
                         <div key={article?.id} className="article__box">
                             {/* 좌측 상단 프로필 영역 */}
                             <div className="article__profile-box">
                                 <img className="article__profile" src={article?.userImage} alt="Author Profile" />
                                 <div className="article__author-name">{article?.email}</div>
                                 <div className="article__date">{article?.createdAt}</div>
                             </div>

                             {/* 우측 상단 태그 영역 */}
                             <div className="article__tag-box">
                                 {/* 해당 아티클에 태그가 있는 경우 */}
                                 {article.tags?.length > 0 && (
                                     article.tags?.map((tag) => (
                                         <div className="article__tag">{tag}</div>
                                     ))
                                 )}
                             </div>

                             {/* 콘텐츠 영역 */}
                             <div className="article__title">
                                 {article?.title}
                             </div>

                             <div className="article__summary">
                                 {article?.summary}
                             </div>

                             {/* 삭제/수정 버튼 영역 */}
                             <div className="article__utils-box">
                                 <div
                                    className="article__delete"
                                    role="presentation"
                                    onClick={() => alert('u click this')}
                                 >
                                     삭제
                                 </div>

                                 <div
                                     className="article__edit"
                                     role="presentation"
                                     onClick={() => alert('u click this')}
                                 >
                                     수정
                                 </div>

                             </div>

                         </div>
                     ))
                     ) : (
                     <div>게시글이 없습니다.</div>
                 )
                }
            </div>
        </>
    )
}