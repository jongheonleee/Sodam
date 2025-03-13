import React, {useContext, useEffect, useState} from 'react';
import ThemeContext from "./context/ThemeContext";
import Router from "./route/Router";


function App() {
    // 다크모드, 화이트 모드 처리
    const context = useContext(ThemeContext)

    // 인증 여부 확인(로그인 되었는지 확인)
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false)

    // 컴포넌트 생성시 인증 여부를 확인하여 업데이트한다.
    useEffect(() => {
        // 로컬 스토리지에 token 값에 값이 존재하는지 확인
        // 존재하면 인증 여부가 된 것이고 그게 아니면 인증이 안된 것
        let token = localStorage.getItem('token')
        console.log('token = ' + token)
        if (token) {
            setIsAuthenticated(true)
        } else {
            setIsAuthenticated(false)
        }
    }, []);

    // 사용자가 로그아웃 버튼을 클릭하면 로컬 스토리지에 저장된 토큰값을 제거한다
    const handleLogout = async (e : React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault()

        let token = localStorage.getItem('token')
        if (token) {
            localStorage.removeItem('token')
            setIsAuthenticated(false)
        } else {
            console.log('로그아웃 실패, 로컬스토리지에 저장된 토큰이 없음')
        }
    }

    return (
      <>
          <div className={context.theme === 'light' ? 'white' : 'dark'}>
              <Router
                  isAuthenticated={isAuthenticated}
                  setIsAuthenticated={setIsAuthenticated}
                  handleLogout={handleLogout}
              />
          </div>
      </>
  );
}

export default App;
