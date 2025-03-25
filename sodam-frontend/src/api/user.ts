import axios from "axios";

const client = axios.create({
    baseURL : 'http://localhost:8080', // 백엔드 현재 로컬에서 돌아가고 있음
});


export const getUserInfo = () => {
    return client.get("/api/v1/users/info",  {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}