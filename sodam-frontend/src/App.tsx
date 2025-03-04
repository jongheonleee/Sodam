import React, {useContext} from 'react';
import ThemeContext from "./context/ThemeContext";
import Router from "./route/Router";
import { worker } from "./mocks/browser"; // MSW worker 가져오기


if (process.env.NODE_ENV === 'development') {
    worker.start();
}

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
