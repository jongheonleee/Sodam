import {useEffect, useState} from "react";

export default function Test() {
    const [message, setMessage] = useState<string>()

    useEffect(() => {
        fetch("http://localhost:8080/api/v1/hello") // Spring Boot 백엔드 호출
            .then((res) => res.json()) // 문자열 데이터 반환
            .then((data) => {
                if (data?.code === 'success') {
                    setMessage(data.data)
                }

                console.log(data.data)
            })
            .catch((error) => console.error("Error fetching data:", error));
    }, []);

    return (
        <div>
            연결 테스트 : { message }
        </div>
    )
}