import { CategoryType } from "../types/article";
import { http, HttpResponse } from 'msw';

export const categoriesHandlers = [
    http.get('/api/categories', ({ request, params }) => {
        return HttpResponse.json({
                code: 200,
                result: "SUCCESS",
                message: "성공적으로 조회",
                data: [
                    {
                        id : "CT001",
                        topId : "CT000",
                        name : "전체",
                        ord : 1,
                        validYN : 0,
                    },
                    {
                        id : "CT002",
                        topId : "CT000",
                        name : "나의 글",
                        ord : 2,
                        validYN : 0,
                    },
                    {
                        id : "CT003",
                        topId : "CT000",
                        name : "커리어",
                        ord : 3,
                        validYN : 0,
                    },
                    {
                        id : "CT004",
                        topId : "CT000",
                        name : "학습법",
                        ord : 4,
                        validYN : 0,
                    },
                    {
                        id : "CT005",
                        topId : "CT000",
                        name : "프로젝트 모집",
                        ord : 5,
                        validYN : 0,
                    },
                ]
            }
        );
    }),
];
