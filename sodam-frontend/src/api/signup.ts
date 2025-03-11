import axios, {AxiosResponse} from "axios";

const client = axios.create({
    baseURL : 'http://localhost:8080', // 백엔드 현재 로컬에서 돌아가고 있음
});

interface SignupRequestProps{
    name : string,
    email : string,
    password : string,
    introduce : string
}

export const signup = (request : SignupRequestProps) => {
    return client.post("/api/v1/auth/signup", request);
}