import {ArticleDetailType} from "../../types/article";
import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import Header from "../../components/Header";
import ArticleDetail from "../../components/ArticleDetail";
import Footer from "../../components/Footer";
import {deleteArticle, dislikeArticle, getDetailArticle, likeArticle} from "../../api/article";
import {deleteComment, getComment, postComment, updateComment} from "../../api/comment";

interface ArticleDetailPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}


export default function ArticleDetailPage({
    handleLogout
} : ArticleDetailPageProps) {
    // url에 있는 articleId 조회
    const { articleId } = useParams();

    // 리다이렉션을 위한 navigate
    const navigate = useNavigate();

    // 현재 회원 정보
    const user = {
        email : "qwefghnm1212@gmail.com"
    }

    // 댓글
    const [comment, setComment] = useState<string>(''); // 이 부분 타입 string -> commentType으로 바꿔야함. 댓글 수정 기능 고려해야함

    // 댓글 id 부분(수정 처리)
    const [commentId, setCommendId] = useState<number>(0);

    // 에러 필드
    const [error, setError] = useState<string>('');


    // 게시글 상세 필드
    const [ articleDetail, setArticleDetail ] = useState<ArticleDetailType | null>(null);

    // 컴포넌트 생성시 articleId와 관련된 게시글 상세 조회 처리
    useEffect(() => {
        // 게시글 상세 필드 초기화
        setArticleDetail(null);

        if (articleId) { // undefined 체크
            getDetailArticle(articleId).then((res) => {
                if (res.status === 200) {
                    setArticleDetail(res.data.data);
                }
                console.log(res.data.data);
            });
        }
    }, [articleId]);



    // 게시글 삭제 핸들링
    const handleArticleDelete = () => {
        // 게시글 삭제 요청을 보냄
        // 게시글을 성공적으로 삭제하면 홈으로 리다이렉션 처리
        fetch(`/api/articles/${articleId}`,{
            method : 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            }})
            .then(res => res.json())
            .then(data => {
                if (data?.result === 'SUCCESS') {
                    alert('게시글 성공적으로 삭제');
                    navigate('/', { replace : true })
                }
            })

        if (articleId) {
            deleteArticle(articleId)
                .then(res => {
                    if (res.status === 200) {
                        alert("게시글이 삭제되었습니다.")
                        navigate("/")
                    }
                })
        }

    }

    // 게시글 좋아요 핸들링
    async function handleArticleLike() {
        // 좋아요 처리 비동기로 api를 호출함
        // 좋아요 처리가 되었음을 알림

        if (articleId) {
            likeArticle(articleId)
                .then(res => {
                    if (res.status === 200) {
                        alert("좋아요를 눌렀습니다.")
                    }
                })
        }
    }


    // 게시글 싫어요 핸들링
    async function handleArticleDislike() {
        // 싫어요 처리 비동기로 api를 호출함
        if (articleId) {
            dislikeArticle(articleId)
                .then(res => {
                    if (res.status === 200) {
                        alert("싫어요를 눌렀습니다.")
                    }
                })
        }
    }

    // 댓글 좋아요 핸들링
    async function handleCommentLike (id: number) {
        // 좋아요 처리 비동기로 api를 호출함
        await fetch(`/api/comments/${id}/like`)
            .then(res => res.json())
            .then(data => {
                if (data?.result === 'SUCCESS') {
                    alert('댓글 좋아요 처리 성공');
                }
            })
            .catch(error => {
                console.error('Error handling like comment');
            })
    }

    // 댓글 싫어요 핸들링
    async function handleCommentDislike(id: number) {
       // 싫어요 처리 비동기로 api를 호출함
        await fetch(`/api/comments/${id}/dislike`)
            .then(res => res.json())
            .then(data => {
                if (data?.result === 'SUCCESS') {
                    alert('댓글 싫어요 처리 성공');
                }
            })
            .catch(error => {
                console.error('Error handling dislike comment');
            })
    }

    // 댓글 삭제 핸들링
    async function handleCommentDelete(commentId : number){
        // 댓글 삭제 비동기로 api를 호출함
        await deleteComment(commentId).then((res) => {
            if (res.status === 200) {
                alert('댓글 삭제 처리')
                setCommendId(0)
            }
        })
    }


    // 댓글 입력 핸들링
    const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setComment(e.target.value);
        setError('');
    }

    // 댓글 수정 버튼 클릭시 핸들링
    async function handleCommentEdit(commentId : number) {
        await getComment(commentId,).then((res) => {
            if (res.status === 200) {
                console.log(res.data.data)
                setCommendId(res.data.data.commentId)
                setComment(res.data.data.comment)
            }
        })
    }


    // 댓글 폼 제출 핸들링
    function handleCommentSubmit(e: React.FormEvent<HTMLFormElement>) {
        // 1차 제출 방지
        e.preventDefault();

        // articleId 존재하는지 확인 - 요청을 보내려면 기본적으로 articleId가 존재해야함
        if (!articleId) {
            setError('게시글 아이디가 조회되지 않습니다.');
            return;
        }

        // 유효성 검증
        if (!(1 <= comment.length)) {
            setError('댓글의 최소 길이는 1글자입니다.');
            return;
        }

        if (commentId !== 0) { // 댓글 수정 - commentId가 존재하면 수정 처리 요청
            return updateComment(
                commentId,
                {'comment': comment}
            ).then((res) => {
                if (res.status === 200) {
                    // 초기화
                    setCommendId(0)
                    setComment('')

                    // 데이터 확인
                    alert("댓글 수정 성공")
                    console.log(res.data.data)

                    // // 네비게이션 처리
                    navigate(`/articles/${articleId}`)
                }
            })
        } else { // 댓글 등록 - commentId가 존재하지 않으면 등록 처리 요청
            return postComment(
                articleId,
                {'comment': comment}
            ).then((res) => {
                if (res.status === 200) {
                    // 초기화
                    setComment('')

                    // 데이터 확인
                    alert("댓글 등록 성공")
                    console.log(res.data.data)

                    // // 네비게이션 처리
                    navigate(`/articles/${articleId}`)
                }
            })
        }

    }

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
                { articleDetail ?
                    <ArticleDetail
                        user={user}
                        articleDetail={articleDetail}
                        comment={comment}
                        handleChange={handleChange}
                        handleArticleDelete={handleArticleDelete}
                        handleArticleLike={handleArticleLike}
                        handleArticleDislike={handleArticleDislike}
                        handleCommentSubmit={handleCommentSubmit}
                        handleCommentLike={handleCommentLike}
                        handleCommentDislike={handleCommentDislike}
                        handleCommentDelete={handleCommentDelete}
                        handleCommentEdit={handleCommentEdit}
                    /> : <div>게시글이 존재하지 않습니다.</div>
                }
            <Footer />
        </>
    )
}