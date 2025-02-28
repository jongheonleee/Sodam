export default function Search() {
    return (
        <div className="article__navigation">
            <div className="article__search-box-right">
                <input
                    type="text"
                    placeholder="검색어를 입력하세요"
                    className="article__search-input"
                    // value={searchTerm}
                    // onChange={(e) => setSearchTerm(e.target.value)}
                />
                <button
                    className="article__search-btn"
                    onClick={() => alert("clicked")}
                >
                    검색
                </button>
            </div>
        </div>
    )
}