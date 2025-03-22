import Header from "../../components/Header";
import Footer from "../../components/Footer";
import SubscriptionCarousel from "../../components/SubscriptionCarousel";
import Secretes from "../../components/Secretes";
import React, {useEffect, useState} from "react";
import {SecreteSummaryType} from "../../types/secret";
import {getSecrets} from "../../api/secret";
import SecretPagination from "../../components/SecretPagination";
import {getArticles} from "../../api/article";

interface SecretesPageProps {
    handleLogout : (e : React.MouseEvent<HTMLButtonElement>) => void,
}


export default function SecretesPage({
    handleLogout,
}: SecretesPageProps) {
    // 시크릿 및 페이징 정보
    const [secrets, setSecrets] = useState<SecreteSummaryType[]>([])
    const [page, setPage] = useState<number>(1)
    const [totalPages, setTotalPages] = useState<number>(1)

    // 특정 부분 변동되면 다시 useEffect 호출하게 만들기
    const [refreshTrigger, setRefreshTrigger] = useState(false);

    const handlePageChange : (event: unknown, value: number) => void = (event, value) => {
        setPage(value) // 바로 page를 사용할 수 없음

        getSecrets(value).then((res) => {
            if (res.status === 200) {
                setSecrets(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
            }
        })
    }

    // 컴포넌트 생성시 fetch 처리
    useEffect(() => {
        // 초기화
        setSecrets([])

        // 조회처리
        getSecrets().then((res) => {
            if (res.status === 200) {
                setSecrets(res.data.data.content)
                setPage(res.data.data.pageNumber)
                setTotalPages(res.data.data.totalPages)
            }
            console.log(res.data.data)
        })

    }, [refreshTrigger])

    return (
        <>
            <Header
                handleLogout={handleLogout}
            />
            <SubscriptionCarousel  />
            <Secretes
                secrets={secrets}
            />
            <SecretPagination
                page={page}
                totalPages={totalPages}
                handlePageChange={handlePageChange}
            />
            <Footer />
        </>
    )
}