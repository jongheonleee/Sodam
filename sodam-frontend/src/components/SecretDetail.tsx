import {SecretDetailType} from "../types/secret";

interface SecretDetailProps {
    secretDetail: SecretDetailType
}

export default function SecretDetail({
    secretDetail
}: SecretDetailProps) {
    return (
        <>
            {secretDetail.content}
        </>
    )
}