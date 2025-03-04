import React from "react";
import { ArticleFormType, Category} from "../types/article";

interface ArticleFormProps {
    categories: Category[];
    tag: string;
    selectedCategory: Category;
    articleForm: ArticleFormType;
    error: string;
    handleSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
    handleChange: (e: React.ChangeEvent<HTMLInputElement> | React.ChangeEvent<HTMLTextAreaElement>) => void;
    selectCategory: (e: React.ChangeEvent<HTMLSelectElement>) => void;
    handleImageUpload: (e: React.ChangeEvent<HTMLInputElement>) => void;
    removeTag: (name: string) => void;
    setTag : (name : string) => void;
    handleTagEnter: (e: React.KeyboardEvent<HTMLInputElement>) => void;
}


export default function ArticleForm({
    categories,
    tag,
    selectedCategory,
    articleForm,
    error,
    handleSubmit,
    handleChange,
    selectCategory,
    handleImageUpload,
    removeTag,
    setTag,
    handleTagEnter,
} : ArticleFormProps) {

    return (
        <form onSubmit={handleSubmit} className="form">
            <div className="form__block">
                <label htmlFor="title">제목</label>
                <input
                    type="text"
                    name="title"
                    id="title"
                    required
                    onChange={e => handleChange(e)}
                    value={articleForm?.title}
                />
            </div>
            <div className="form__block">
                <label htmlFor="category">카테고리</label>
                <select
                    name="categories"
                    id="categories"
                    value={selectedCategory.id}  // 선택된 카테고리 값 바인딩
                    onChange={e => selectCategory(e)}  // onChange 핸들러
                >
                    <option value="">카테고리를 선택해주세요</option>
                    {categories.map((category) => (
                        <option
                            key={category?.id}
                            value={category?.id}  // 카테고리 ID를 value로 설정
                        >
                            {category?.name}
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
                    onChange={handleChange}
                    value={articleForm?.summary}
                />
            </div>
            <div className="form__block">
                <label htmlFor="content">내용</label>
                <textarea
                    name="content"
                    id="content"
                    required
                    onChange={handleChange}
                    value={articleForm?.content}
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
                    onChange={handleImageUpload}
                />
            </div>

            <div className="form__block">
                <label htmlFor="hashtags">태그</label>
                <input
                    type="text"
                    name="hashtags"
                    id="hashtags"
                    placeholder="태그 입력 후 Enter"
                    onKeyDown={e => handleTagEnter(e)}
                    onChange={e => setTag(e.target.value)}
                    value={tag}
                />
                <div className="hashtag-list">
                    {articleForm?.tags?.map((tag) => (
                        <div className="hashtag-item">
                            #{tag}
                            <button type="button" onClick={() => removeTag(tag)}>x</button>
                        </div>
                    ))}
                </div>
            </div>

            {/* 에러 표시 영역 */}
            {error && error?.length > 0 && (
                <div className="form__block">
                    <div className="form__error">{error}</div>
                </div>
            )}

            <div className="form__block">
                <input
                    type="submit"
                    className="form__btn--submit"
                />
            </div>
        </form>
    )
}