import Header from "../../components/Header";
import ArticleForm from "../../components/ArticleForm";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {ArticleFormType, CategoryType} from "../../types/article";

export default function ArticleNewPage() {
    // 리다이렉션을 위한 navigate
    const navigate = useNavigate();

    // 해당 컴포넌트 생성시 카테고리 조회해 오기
    const [categories, setCategories] = useState<CategoryType[]>([]);

    // 입력된 태그 필드 선언
    const [tag, setTag] = useState<string>('');

    // 기본으로 선정되어 있는 카테고리 선언
    const [selectedCategory, setSelectedCategory] = useState<CategoryType>({
        id : "CT001",
        topId : "CT000",
        name : "전체",
        ord : 1,
        validYN : 0,
    })

    // 게시글 생성 폼 필드 선언
    const [articleForm, setArticleForm] = useState<ArticleFormType>({
        title : '',
        category : null,
        summary : '',
        content : '',
        images : null,
        tags : []
    })

    // 에러 발생시 알림 메시지
    const [error, setError] = useState<string>('');


    // 폼 제출 핸들링 함수
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault(); // 기본 폼 제출 동작 방지

        // 유효성 검증
        // - 검증해야 할 필드 : 제목, 요약글, 내용, 카테고리
        // 제목은 최소 5글자에서 최대 25글자로 구성되어야 한다
        // 요약글은 최소 5글자에서 최대 500글자로 구성되어야 한다.
        // 내용은 최소 5글자로 구성되어야 한다.
        // 카테고리는 반드시 존재하는 카테고리로 선정되어야 한다.
        if (!(5 <= articleForm.title.length && articleForm.title.length <= 25)) {
            setError('제목은 최소 5글자에서 최대 25글자로 구성되어야 합니다.');
            return;
        }

        if (!(5 <= articleForm.summary.length && articleForm.summary.length <= 500)) {
            setError('요약글은 최소 5글자에서 최대 500글자로 구성되어야 한다.');
            return;
        }

        if (!(5 <= articleForm.content.length && articleForm.content.length <= 500)) {
            setError('내용은 최소 5글자로 구성되어야 합니다.');
            return;
        }

        if (!articleForm.category) {
            setError('카테고리는 반드시 존재하는 카테고리로 선정되어야 합니다.');
            return;
        }

        // 에러 필드 초기화
        setError('');

        const formData = new FormData();

        formData.append('title', articleForm.title);
        formData.append('summary', articleForm.summary);
        formData.append('content', articleForm.content);
        formData.append('category', articleForm.category.id);

        // 태그 추가
        if (articleForm.tags) {
            articleForm.tags.forEach((tag : string) => formData.append('tags', tag));
        }

        // 이미지 추가
        if (articleForm.images) {
            articleForm.images.forEach((image : File) => formData.append('images', image));
        }

        try {
            const response = await fetch('/api/articles', {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                throw new Error("게시글 등록에 실패했습니다.");
            }

            const data = await response.json();
            console.log(data);

            if (data?.result === 'SUCCESS' && data.data) {
                // 게시글 등록 성공시 알림
                alert("게시글 등록이 완료되었습니다.");

                // 홈으로 리다이렉션
                navigate('/', {replace : true})
            }

        } catch (error) {
            console.error('게시글 등록 오류 : ', error);
            setError("게시글 등록 오류 : " + error);
        }
    };


    // 인풋 입력값 변경시 핸들링 함수
    const handleChange = (e: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLTextAreaElement>) => {
        // 입력값 업데이트
        setArticleForm({
            ...articleForm,
            [e.target.name] : e.target.value
        });

        console.log(articleForm);

        // 에러 필드 초기화
        setError('');
    }



    // 카테고리 선택시 핸들링 함수
    const selectCategory = (e: React.ChangeEvent<HTMLSelectElement>) => {
        // 선택한 카테고리 조회
        const selectedCategory = categories.find(category => category.id === e.target.value);

        // 만약 카테고리가 없으면 에러로 알림
        if (!selectedCategory) {
            setError('선택한 카테고리가 존재하지 않습니다.');
            return;
        }

        // 그게 아니면 선택한 카테고리로 변경
        console.log(selectedCategory);
        setSelectedCategory(selectedCategory);  // 선택된 카테고리 ID로 상태 업데이트
        // 선택된 카테고리 폼 데이터에 적용
        setArticleForm({
            ...articleForm,
            category : selectedCategory
        })
    };

    // 이미지 업로드시 핸들링 함수
    const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        // 업로드된 이미지가 존재하는지 확인
        if (!e.target.files || e.target.files.length === 0) {
            console.log('업로드된 프로필 이미지가 없습니다.');
            return;
        }

        // 등록한 이미지 조회
        const files = e.target.files;
        const imagesArray = Array.from(files);  // 선택된 파일들을 배열로 변환

        // 해당 이미지가 존재하면 필드 업데이트
        setArticleForm({
            ...articleForm,
            images: imagesArray
        });

        // 에러 메시지 초기화
        setError('');
    };

    // 임시 등록된 태그 삭제 함수
    const removeTag = (name : string) => {
        // 선택된 태그와 다른 태그들만 남겨 놓기
        const newTags = articleForm?.tags.filter((tagName : string) => tagName !== name);

        // 태그 필드 업데이트
        setArticleForm({
            ...articleForm,
            tags: newTags
        });

        console.log(articleForm);

        // 에러 필드 초기화
        setError('');
    }


    // 새로운 태그 등록 함수
    const addTag = (name : string) => {
        // 이미 등록된 태그면 호출 중단
        if (name && articleForm.tags.includes(name)) {
            // 에러 알림
            setError('중복된 태그를 등록할 수 없습니다.');
            // 태그 필드값 초기화
            setTag('');
            return;
        }

        // 새로운 태그 배열 생성
        const newTags = [...articleForm.tags, tag];
        console.log(newTags);

        // 태그 필드 업데이트
        setArticleForm({
            ...articleForm,
            tags: newTags
        });

        // 태그 필드값 초기화
        setTag('');
        // 에러 필드 초기화
        setError('');

        console.log(articleForm);
    }

    const handleTagEnter = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            e.preventDefault(); // 기본 동작인 폼 제출 방지
            addTag(tag); // 태그 추가
            setTag(''); // 태그 초기화
        }
    }


    // 컴포넌트 생성시 categories 요청 받아오기
    useEffect(() => {
        // 카테고리 배열 초기화
        setCategories([]);

        // 카레고리 데이터 요청 받아오기
        fetch('/api/categories')
            .then(res => res.json())
            .then(data => {
                if (data?.result === 'SUCCESS') {
                    setCategories(data.data);
                }

                console.log(data.data);
            })
            .catch(error => {
                console.error('Error fetching data', error);
            })
    }, []);


    return (
        <>
            <Header />
            <ArticleForm
                categories = {categories}
                tag = {tag}
                selectedCategory = {selectedCategory}
                articleForm = {articleForm}
                error = {error}
                handleSubmit = {handleSubmit}
                handleChange = {handleChange}
                selectCategory = {selectCategory}
                handleImageUpload = {handleImageUpload}
                removeTag = {removeTag}
                setTag = {setTag}
                handleTagEnter = {handleTagEnter}
            />
        </>
    )
}