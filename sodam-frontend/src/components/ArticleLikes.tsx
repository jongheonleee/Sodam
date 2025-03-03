import Search from "./Search";
import Articles from "./Articles";
import {useState} from "react";
import {ArticleSummary} from "../types/article";

export default function ArticleLikes() {

    // 게시글
    const [articles, setArticles] = useState<ArticleSummary[]>([]);


    return (
        <>
            <Search />
            <Articles
                articles={articles}
            />
        </>
    )
}