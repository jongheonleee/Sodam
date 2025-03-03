import { http, HttpResponse } from 'msw';


export const authHandlers = [
    // 로그인
    http.post('/api/auth/login', ({ request, params }) => {
        return HttpResponse.json({
            code : 200,
            result : 'SUCCESS',
            message : '로그인 성공',
            data : {
                email : "qwefghnm1212@gmail.com",
                name : 'yeonuel',
                profileImage: {
                    url : "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    alt : "테스트용"
                },
                introduce : "안녕하세요 풀스택 AI 서비스 개발자를 목표로 학습하고 있는 28살 취준생입니다. 🧑🏻‍💻",
                token : "dalndlwbg!@#4242#addaw"
            }
        })
    })
]