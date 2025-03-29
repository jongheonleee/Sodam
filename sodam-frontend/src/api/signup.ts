import axios from "axios";

const client = axios.create({
    baseURL : 'https://localhost:8443', // 백엔드 현재 로컬에서 돌아가고 있음
});

interface SignupRequestProps{
    name : string,
    email : string,
    password : string,
    introduce : string,
    positionId: string,
}

export const signup = (request : SignupRequestProps) => {
    return client.post('/api/v1/auth/signup', request)
}