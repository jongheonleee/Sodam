// 게시글과 관련된 데이터 타입 정의
// - 메인 페이지에서 보여줄 게시글 타입
// - id, category, email, author, createdAt, userImage, title, summary, tags
// - 추가로 정의해야 하는 타입 => userImage(이미지), tags(tag 배열)
export interface ImageType {
    url: string; // aws s3 저장된 url 경로
    alt?: string; // 비고란
}

export interface TagType {
    id: number;
    articleId: number; // 연관된 게시글
    name: string; // 태그 이름
}

export interface CategoryType {
    categoryId: string
    topCategoryId: string // 상위 카테고리 id
    categoryName: string
    isValid: number // 사용 가능 여부(0, 1)
}

export interface ArticleSummaryType {
    articleId: number
    username: string
    profileImageUrl: string
    title: string,
    summary: string,
    createdAt: string,
    tags: string[],
}

// 댓글 타입 정의
export interface CommentType {
    id: number;
    articleId: number;
    profileImage: ImageType;
    email: string;
    createdAt: string;
    content: string;
    likeCnt: number;
    dislikeCnt: number;
}

export interface ArticleDetailType {
    id: number;
    title: string;
    email: string;
    author: string;
    createdAt: string;
    content: string;
    profileImage: ImageType;
    category: CategoryType;
    viewCnt: number;
    likeCnt: number;
    dislikeCnt: number;
    images : ImageType[];
    tags: TagType[];
    comments: CommentType[];
}

// 게시글 작성 폼 타입
// - 제목, 카테고리, 요약, 이미지 여러개, 태그
export interface ArticleFormType {
    id?: number;
    title: string;
    category: CategoryType | null;
    summary: string;
    content: string;
    images : File[] | null;
    tags : string[];
}

// 게시글 생성 요청
export interface ArticleCreateRequest {
    categoryId: string
    title: string
    summary: string
    content: string
    tags: String[]
}

