import axios from "axios";

const client = axios.create({
    baseURL : 'https://localhost:8443', // 백엔드 현재 로컬에서 돌아가고 있음
    withCredentials: true // cors 관련 부분
});

export const getReissuedToken = () => {
    return client.post(`/api/v1/reissue`, {}, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            "token": localStorage.getItem("token"),
            "refresh_token": localStorage.getItem("refresh_token"),
        },
    })
}