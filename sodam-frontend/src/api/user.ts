import axios from "axios";

const client = axios.create({
    baseURL : 'https://localhost:8443', // 백엔드 현재 로컬에서 돌아가고 있음
});


export const getUserInfo = () => {
    return client.get("/api/v1/users/info",  {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}

interface UserInfoUpdateRequest{
    name : string,
    email : string,
    password : string,
    introduce : string,
    positionId: string,
}

export const updateUserInfo = (request: UserInfoUpdateRequest) => {
    return client.put("/api/v1/users/info", request, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}