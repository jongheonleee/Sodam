import Header from "../../components/Header";
import Footer from "../../components/Footer";
import React, {useEffect, useState} from "react";
import SecretDetail from "../../components/SecretDetail";
import {useParams} from "react-router-dom";
import {SecretDetailType} from "../../types/secret";
import {getDetailSecret} from "../../api/secret";

interface SecretDetailPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}

export default function SecretDetailPage({
    handleLogout,
} : SecretDetailPageProps) {
    const { secretId } = useParams()
    const [ refreshTrigger, setRefreshTrigger ] = useState(false)
    const [ secretDetail, setSecretDetail ] = useState<SecretDetailType | null>(null)

    useEffect(() => {
        setSecretDetail(null)

        if (secretId) {
            getDetailSecret(parseInt(secretId)).then((res) => {
                if (res.status === 200) {
                    setSecretDetail(res.data.data)
                }
                console.log(res.data.data);
            })
        }

    }, [secretId, refreshTrigger]);

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            { secretDetail ?
                <SecretDetail
                    secretDetail={secretDetail}
                /> : <div>구독자 전용 게시글이 존재하지 않습니다.</div>
            }
            <Footer />
        </>
    )
}