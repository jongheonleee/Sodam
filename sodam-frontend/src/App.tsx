import React, {useContext} from 'react';
import ThemeContext from "./context/ThemeContext";
import Router from "./components/Router";

function App() {
    // 다크모드, 화이트 모드 처리
    const context = useContext(ThemeContext);

    // auth 체크, 추후에 context에서 개발해서 활용
    return (
      <>
          <div className={context.theme === 'light' ? 'white' : 'dark'}>
              <Router />
          </div>
      </>
  );
}

export default App;
