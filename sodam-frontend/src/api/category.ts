import axios from "axios";

const client = axios.create({
    baseURL : 'https://localhost:8443', // 백엔드 현재 로컬에서 돌아가고 있음
});


export const getCategories = () => {
    return client.get("/api/v1/categories",  {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
        }
    })
}