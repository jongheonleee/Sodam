// 프로필 이미지
export interface Image {
    url: string; // aws s3 저장된 url 경로
    alt?: string; // 비고란
}

// 단순 회원 정보
export interface User {
    email : string;
    name : string;
    profileImage : Image;
    introduce : string;
    token : string;
}