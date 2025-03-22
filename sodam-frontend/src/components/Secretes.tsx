import {SecreteSummaryType} from "../types/secret";
import {Link} from "react-router-dom";

interface SecretsProps {
    secrets: SecreteSummaryType[]
}

export default function Secretes({
    secrets,
} : SecretsProps) {
    return (
        <>
            <div className="secret__list">
                {secrets?.length > 0 ? (
                    secrets?.map((secret) => (
                        <div key={secret?.secretId} className="secret__box">
                            {/* 글 내용 (좌측) */}
                            <div className="secret__content">
                                <div className="secret__profile-box">
                                    <img className="secret__profile" src={secret?.profileImageUrl} alt="Author Profile" />
                                    <div className="secret__author-name">{secret?.username}</div>
                                    <div className="secret__date">{secret?.createdAt}</div>
                                </div>

                                <Link to={`/secrets/${secret?.secretId}`}>
                                    <div className="secret__title">{secret?.title}</div>
                                    <div className="secret__summary">{secret?.summary}</div>
                                </Link>
                            </div>

                            {/* 태그 + 이미지 포함하는 우측 영역 */}
                            <div className="secret__right-box">
                                <div className="secret__tag-box">
                                    {secret.tags?.length > 0 &&
                                        secret.tags?.map((tag) => <div className="secret__tag" key={tag}>{tag}</div>)}
                                </div>
                                <div className="secret__thumbnail-box">
                                    <img className="secret__thumbnail" src={secret.thumbnailUrl} alt="Secret Thumbnail" />
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <div>구독자 전용 게시글이 없습니다.</div>
                )}
            </div>
        </>


    )
}