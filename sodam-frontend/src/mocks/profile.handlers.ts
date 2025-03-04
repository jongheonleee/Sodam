import { http, HttpResponse } from 'msw';

export const profileHandlers = [
    // 게시글 서머리 조회 처리
    http.get('/api/profile/qwefghnm1212@gmail.com', ({ request, params }) => {
        // 응답을 그대로 반환
        return HttpResponse.json({
                code : 200,
                result : 'SUCCESS',
                message : "조회 성공",
                data : [
                    // 추가적으로 사용자 프로필 정보도 같이 반환해야함
                    {
                        id: 1,
                        email: "qwefghnm1212@gmail.com",
                        author: "qwefghnm1212",
                        createdAt: "2025. 02. 25 오후 03:35:01",
                        profileImage: {
                            url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt : "테스트용"
                        },
                        title: "테스트 게시글 제목",
                        summary: "테스트 게시글 요약글",
                        tags : [
                            {
                                id: 1,
                                articleId : 1,
                                name : "커리어"
                            },
                            {
                                id: 2,
                                articleId : 1,
                                name : "일상"
                            },
                            {
                                id: 3,
                                articleId : 1,
                                name : "합격"
                            },
                        ],
                    },
                    {
                        id: 2,
                        email: "qwefghnm1212@gmail.com",
                        author: "qwefghnm1212",
                        createdAt: "2025. 02. 25 오후 03:35:01",
                        profileImage: {
                            url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt : "테스트용"
                        },
                        title: "테스트 게시글 제목",
                        summary: "테스트 게시글 요약글",
                        tags : [
                            {
                                id: 1,
                                articleId : 1,
                                name : "커리어"
                            },
                            {
                                id: 2,
                                articleId : 1,
                                name : "일상"
                            },
                            {
                                id: 3,
                                articleId : 1,
                                name : "합격"
                            },
                        ],
                    },
                    {
                        id: 3,
                        email: "qwefghnm1212@gmail.com",
                        author: "qwefghnm1212",
                        createdAt: "2025. 02. 25 오후 03:35:01",
                        profileImage: {
                            url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt : "테스트용"
                        },
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
                        profileImage: {
                            url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt : "테스트용"
                        },
                        title: "테스트 게시글 제목",
                        summary: "테스트 게시글 요약글",
                        tags : [
                            {
                                id: 1,
                                articleId : 1,
                                name : "커리어"
                            },
                            {
                                id: 2,
                                articleId : 1,
                                name : "일상"
                            },
                            {
                                id: 3,
                                articleId : 1,
                                name : "합격"
                            },
                        ],
                    },
                    {
                        id: 5,
                        email: "qwefghnm1212@gmail.com",
                        author: "qwefghnm1212",
                        createdAt: "2025. 02. 25 오후 03:35:01",
                        profileImage: {
                            url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt : "테스트용"
                        },
                        title: "테스트 게시글 제목",
                        summary: "테스트 게시글 요약글",
                        tags : [
                            {
                                id: 1,
                                articleId : 1,
                                name : "커리어"
                            },
                            {
                                id: 2,
                                articleId : 1,
                                name : "일상"
                            },
                            {
                                id: 3,
                                articleId : 1,
                                name : "합격"
                            },
                        ],
                    }
                ]
            }
        );
    }),
];
