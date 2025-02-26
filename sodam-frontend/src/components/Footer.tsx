import {Link} from "react-router-dom";
import {useContext} from "react";
import ThemeContext from "../context/ThemeContext";
import { BsSun, BsMoonFill } from "react-icons/bs";


export default function Footer() {
    // 컨텍스트 조회하여 다크모드/화이트모드 처리함
    const context = useContext(ThemeContext);
    console.log(context);
    console.log(context.theme);
    console.log(localStorage.theme);
    context.toggleMode();

    return (
        <footer>
            <Link to="/articles/new">글쓰기</Link>
            <Link to="/articles">게시글</Link>
            <Link to="/profile">프로필</Link>
            <Link to="/subscription">구독권</Link>

            <>
                {context.theme === 'light' ? (
                    <BsSun
                        className="footer__theme-btn"
                        onClick={() => {
                            console.log('Toggle mode clicked');
                            context.toggleMode();
                        }}
                    />
                ) : (
                    <BsMoonFill
                        className="footer__theme-btn"
                        onClick={() => {
                            console.log('Toggle mode clicked');
                            context.toggleMode();
                        }}
                    />
                )}
            </>
        </footer>
    );
}