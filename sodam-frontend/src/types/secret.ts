export interface SecreteSummaryType {
    secretId: number
    username: string
    profileImageUrl: string
    title: string
    summary: string
    createdAt: string
    thumbnailUrl: string
    tags: string[]
    thumbnail: string
}

//    val secretId: Long,
//     val author: String,
//     val title: String,
//     val content: String,
//     val createdAt: String,
//     val tags: List<TagResponse>,
//     val comments: List<CommentResponse>,
//     val images: List<String>,
//     val secretLikedCnt: Long,
//     val secretDislikeCnt: Long,
//     val secretViewCnt: Long
export interface TagType {
    tagId: number;
    secretId: number; // 연관된 게시글
    tagName: string; // 태그 이름
}

export interface CommentType {
    commentId: number
    secretId: number
    profileImageUrl: string
    userName: string
    createdAt: string;
    content: string;
    commentLikeCnt: number;
    commentDislikeCnt: number;
}

export interface SecretDetailType {
    secretId: number
    author: string
    title: string
    content: string
    createdAt: string
    tags: TagType[]
    comments: CommentType[]
    images: string[]
    secretLikeCnt: number
    secretDislikeCnt: number
    secretViewCnt: number
}