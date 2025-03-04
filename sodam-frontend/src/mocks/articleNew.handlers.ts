import { http, HttpResponse } from 'msw';


export const articleNewHandlers = [
    // 게시글 등록 요청 처리
    http.post('/api/articles', ({request, params}) => {
        return HttpResponse.json({
            code : 200,
            result : 'SUCCESS',
            message : '게시글 등록 성공',
            data : {
                id : 1,
                title : '테스트용 게시글 제목',
                category : {

                },
                summary : '테스트용 게시글 요약',
                content : '테스트용 게시글 내용',
                images: [{
                    url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    alt : "테스트용 이미지"
                },{
                    url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    alt : "테스트용 이미지"
                }],
                tags: [{
                    id: 1,
                    name: "테스트용 태그1",
                    articleId : 1
                },{
                    id: 2,
                    name: "테스트용 태그2",
                    articleId : 1
                }]
            }
        })
    }),
]