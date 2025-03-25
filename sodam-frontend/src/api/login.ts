import axios from "axios";

const client = axios.create({
    baseURL : 'https://localhost:8443', // 백엔드 현재 로컬에서 돌아가고 있음
});

interface LoginRequestProps{
    email : string,
    password : string,
}

export const login = (request : LoginRequestProps) => {
    return client.post("/api/v1/auth/login", request);
}