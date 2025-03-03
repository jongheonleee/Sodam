import {useState} from "react";



export const CATEGORIES = [
    "전체",
    "나의 글",
    "커리어",
    "학습법",
    "프로젝트 모집"
]

interface Hashtag {
    key?: string,
    content?: string,
}


export default function ArticleForm() {
    // 작성해야할 필드
    const [title, setTitle] = useState('')
    const [category, setCategory] = useState('')
    const [summary, setSummary] = useState('')
    const [hashtags, setHashtags] = useState<Hashtag[]>(
        [
            {key : '1', content: "미친 이종헌"},
            {key : '2', content: "풀스택에 AI까지 설립함..."},
            {key : '3', content: "우린 뭐 먹고 살라고 텐련아"},
        ]
    )

    const onSubmit = () => {
        alert("u clicked!!");
    }

    const onChange = () => {
        alert("u changed!!");
    }

    const onHashtagKeyDown = () => {
        alert("dede");
    }

    const removeHashtag = (key: string) => {
        alert("wow");
    }


    return (
        <form onSubmit={onSubmit} className="form">
            <div className="form__block">
                <label htmlFor="title">제목</label>
                <input
                    type="text"
                    name="title"
                    id="title"
                    required
                    onChange={onChange}
                    value={title}
                />
            </div>
            <div className="form__block">
                <label htmlFor="category">카테고리</label>
                <select
                    name="category"
                    id="category"
                    onChange={onChange}
                    defaultValue={category}
                >
                    <option value="">카테고리를 선택해주세요</option>
                    {CATEGORIES?.map((category) => (
                        <option value={category} key={category}>
                            {category}
                        </option>
                    ))}
                </select>
            </div>
            <div className="form__block">
                <label htmlFor="summary">요약</label>
                <input
                    type="text"
                    name="summary"
                    id="summary"
                    required
                    onChange={onChange}
                    value={summary}
                />
            </div>
            <div className="form__block">
                <label htmlFor="content">내용</label>
                <textarea
                    name="content"
                    id="content"
                    required
                    onChange={onChange}
                />
            </div>

            <div className="form__block">
                <label
                    htmlFor="image"
                    className="file-label"
                >
                    이미지 첨부
                </label>
                <input
                    type="file"
                    name="image"
                    id="image"
                    className="file-input"
                    accept="image/*"
                    onChange={onChange}
                />
            </div>

            <div className="form__block">
                <label htmlFor="hashtags">해시태그</label>
                <input
                    type="text"
                    name="hashtags"
                    id="hashtags"
                    placeholder="해시태그 입력 후 Enter"
                    onKeyDown={onHashtagKeyDown}
                />

                <div className="hashtag-list">
                    {hashtags?.map((hashtag) => (
                        <div key={hashtag.key} className="hashtag-item">
                            #{hashtag.content}
                            <button type="button" onClick={() => removeHashtag(hashtag.key!)}>x</button>
                        </div>
                    ))}
                </div>
            </div>

            <div className="form__block">
                <input
                    type="submit"
                    className="form__btn--submit"
                />
            </div>
        </form>
    )
}