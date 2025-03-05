import { http, HttpResponse } from 'msw';


export const articleDetailHandlers = [
    // 게시글 상세 조회 처리
    http.get('/api/articles/1', ({ request, params}) => {
        return HttpResponse.json({
            code : 200,
            result : 'SUCCESS',
            message : '게시글 상세 조회 성공',
            data : {
                id : 1,
                title : "테스트용 제목",
                email : "qwefghnm1212@gmail.com",
                author : "yeonuel",
                createdAt : "2025 02. 28. 오후 03:55:31",
                content : "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum..",
                profileImage : {
                    url: "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    alt: "테스트용 작성자 프로필 이미지"
                },
                category : {
                    id : 'CT001',
                    topId : 'CT000',
                    name : '전체',
                    ord : 1,
                    validYN : 0,
                },
                viewCnt : 100,
                likeCnt : 50,
                dislikeCnt : 12,
                images : [{
                    url: "https://images.unsplash.com/photo-1520085601670-ee14aa5fa3e8?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    alt: "테스트용 게시글 이미지1"
                }, {
                    url: "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=2672&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    alt: "테스트용 게시글 이미지 2"
                }],
                tags : [{
                    id : 1,
                    articleId : 1,
                    name : "테스트용 태그1",
                }, {
                    id : 2,
                    articleId : 1,
                    name : "테스트용 태그2",
                },
                ],
                comments : [
                    {
                        id: 1,
                        articleId : 1,
                        profileImage : {
                            url: "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt: "테스트용 댓글 작성자 프로필 이미지1"
                        },
                        email : "qwefghnm1212@gmail.com",
                        createdAt : "2025 02. 28. 오후 03:55:31",
                        content : "테스트용 댓글 1",
                        likeCnt : 4,
                        dislikeCnt : 1
                    },
                    {
                        id: 2,
                        articleId : 1,
                        profileImage : {
                            url: "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt: "테스트용 댓글 작성자 프로필 이미지1"
                        },
                        email : "wqewqref@gmail.com",
                        createdAt : "2025 02. 28. 오후 03:55:31",
                        content : "테스트용 댓글 1",
                        likeCnt : 4,
                        dislikeCnt : 1
                    },
                    {
                        id: 3,
                        articleId : 1,
                        profileImage : {
                            url: "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt: "테스트용 댓글 작성자 프로필 이미지1"
                        },
                        email : "wqewqref@gmail.com",
                        createdAt : "2025 02. 28. 오후 03:55:31",
                        content : "테스트용 댓글 1",
                        likeCnt : 4,
                        dislikeCnt : 1
                    },
                    {
                        id: 4,
                        articleId : 1,
                        profileImage : {
                            url: "https://images.unsplash.com/photo-1526493356079-3552df24f410?q=80&w=3456&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                            alt: "테스트용 댓글 작성자 프로필 이미지1"
                        },
                        email : "wqewqref@gmail.com",
                        createdAt : "2025 02. 28. 오후 03:55:31",
                        content : "테스트용 댓글 1",
                        likeCnt : 4,
                        dislikeCnt : 1
                    },
                ]
            }
        })
    }),

    // 게시글 좋아요 처리
    http.get('/api/articles/1/like', ({ request, params }) => {
        return HttpResponse.json({
            code: 200,
            result: 'SUCCESS',
            message : '게시글 좋아요 처리 성공',
            data : null,
        })
    }),

    // 게시글 싫어요 처리
    http.get('/api/articles/1/dislike', ({ request, params }) => {
        return HttpResponse.json({
            code: 200,
            result: 'SUCCESS',
            message : '게시글 싫어요 처리 성공',
            data : null,
        })
    }),

    // 게시글의 특정 댓글 좋아요 처리
    http.get('/api/comments/1/like', ({ request, params }) => {
        return HttpResponse.json({
            code: 200,
            result: 'SUCCESS',
            message : '댓글 좋아요 처리 성공',
            data : null,
        })
    }),

    // 게시글의 특정 댓글 싫어요 처리
    http.get('/api/comments/1/dislike', ({ request, params }) => {
        return HttpResponse.json({
            code: 200,
            result: 'SUCCESS',
            message : '댓글 싫어요 처리 성공',
            data : null,
        })
    }),

    // 게시글 삭제 처리
    http.delete('/api/articles/1', ({ request, params }) => {
        return HttpResponse.json({
            code: 200,
            result : 'SUCCESS',
            message : '게시글 삭제 성공',
            data : null,
        })
    }),

    // 게시글의 특정 댓글 조회 /api/comments/1
    http.get('/api/comments/1', ({ request, params}) => {
        return HttpResponse.json({
            code : 200,
            result : 'SUCCESS',
            message : '댓글 삭제 성공',
            data : '조회된 댓글',
        })
    }),

    // 게시글의 특정 댓글 삭제 처리
    http.delete('/api/comments/1', ({ request, params }) => {
        return HttpResponse.json({
            code : 200,
            result : 'SUCCESS',
            message : '댓글 삭제 성공',
            data : null,
        })
    }),

    // 댓글 등록 처리
    http.post('/api/comments', ({ request, params }) => {
        return HttpResponse.json({
            code : 200,
            result : 'SUCCESS',
            message : '댓글 등록 성공',
            data : '등록된 댓글 내용',
        })
    }),

    // 댓글 수정 처리
    http.put('/api/comments/1', ({ request, params }) => {
        return HttpResponse.json({
            code : 200,
            result : 'SUCCESS',
            message : '댓글 등록 성공',
            data : '수정된 댓글 내용',
        })
    })
]