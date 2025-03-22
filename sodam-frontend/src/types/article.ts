// 게시글과 관련된 데이터 타입 정의
// - 메인 페이지에서 보여줄 게시글 타입
// - id, category, email, author, createdAt, userImage, title, summary, tags
// - 추가로 정의해야 하는 타입 => userImage(이미지), tags(tag 배열)
export interface ImageType {
    url: string; // aws s3 저장된 url 경로
    alt?: string; // 비고란
}

export interface TagType {
    tagId: number;
    articleId: number; // 연관된 게시글
    tagName: string; // 태그 이름
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
    title: string
    summary: string
    createdAt: string
    tags: string[]
}


export interface CommentType {
    commentId: number
    articleId: number
    profileImageUrl: string
    userName: string
    createdAt: string;
    content: string;
    commentLikeCnt: number;
    commentDislikeCnt: number;
}

export interface ArticleDetailType {
    userId: string
    articleId: number
    title: string
    profileImageUrl: string
    author: string
    content: string
    createdAt: string
    tags: TagType[]
    comments: CommentType[]
    images: string[] //
    articleLikeCnt: number
    articleDislikeCnt: number
    articleViewCnt: number
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

// 게시글 수정 요청
export interface ArticleUpdateRequest {
    categoryId: string
    title: string
    summary: string
    content: string
    tags: String[]
}

