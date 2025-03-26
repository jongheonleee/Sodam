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

// 회원가입 요청 폼 데이터 타입
export interface SignupProps {
    email: string;
    name: string;
    password: string;
    checkPassword: string;
    profileImage: File | null;  // 업로드할 파일
    profileImageUrl?: string;   // S3 업로드 후 반환된 URL
    introduce: string;
}

// 회원 프로필 타입
export interface UserProfileInfoType {
    userId: string
    articleTotalCnt: number
    email: string | null
    introduce: string
    grade: string
    name: string
    profileImageUrl: string
    ranking: number
    subscription: string

}