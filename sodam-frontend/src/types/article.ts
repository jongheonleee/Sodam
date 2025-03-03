// 게시글과 관련된 데이터 타입 정의
// - 메인 페이지에서 보여줄 게시글 타입
// - id, category, email, author, createdAt, userImage, title, summary, tags
// - 추가로 정의해야 하는 타입 => userImage(이미지), tags(tag 배열)
export interface Image {
    url: string; // aws s3 저장된 url 경로
    alt?: string; // 비고란
}

export interface Tag {
    id: number;
    articleId: number; // 연관된 게시글
    name: string; // 태그 이름
}

export interface Category {
    id: string;
    topId: string; // 상위 카테고리 id
    name: string;
    ord: number; // 정렬 순서
    validYN: number; // 사용 가능 여부(0, 1)
}

export interface ArticleSummary {
    id: number;
    category: Category; // category 타입 정의 해야함
    email: string;
    author: string;
    createdAt: string;
    profileImage: Image; // 이미지 타입
    title: string;
    content: string;
    tags: Tag[]; // 태그 배열
}

// 댓글 타입 정의
export interface Comment {
    id: number;
    profileImage: Image;
    email: string;
    createdAt: string;
    content: string;
    likeCnt: number;
    dislikeCnt: number;
}

export interface ArticleDetail {
    id: number;
    title: string;
    email: string;
    author: string;
    createdAt: string;
    content: string;
    profileImage: Image;
    category: Category;
    viewCnt: number;
    likeCnt: number;
    dislikeCnt: number;
    images : Image[];
    tags: Tag[];
    comments: Comment[];
}

export interface ArticlesProps {
    hasNavigation: boolean;
    defaultTab : string;
}