import axios from "axios";

const client = axios.create({
    baseURL : 'http://localhost:8080', // 백엔드 현재 로컬에서 돌아가고 있음
});

export const getSecrets = (pageNumber : number = 1) => {
    return client.get(`/api/v1/secrets?page=${pageNumber-1}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}