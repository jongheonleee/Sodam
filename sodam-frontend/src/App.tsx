import React, {useContext} from 'react';
import { Route, Routes, Navigate, Link} from "react-router-dom";
import Footer from './components/Footer';
import ThemeContext, {ThemeContextProvider} from "./context/ThemeContext";
import Header from "./components/Header";
import Home from "./pages/home/index.";

function App() {
    // 다크모드, 화이트 모드 처리
    const context = useContext(ThemeContext);

    // auth 체크, 추후에 context에서 개발해서 활용

    return (
      <>
          <div className={context.theme === 'light' ? 'white' : 'dark'}>
              {/*<Header />*/}

              <Routes>
                  <Route path="/" element={<Home />} />
                  <Route path="/articles" element={<h1>Articles</h1>} />
                  <Route path="/articles/:id" element={<h1>Articles Detail</h1>} />
                  <Route path="/articles/new" element={<h1>Articles New Page</h1>} />
                  <Route path="/articles/edit/:id" element={<h1>Articles Edit Page</h1>} />
                  <Route path="/articles/like" element={<h1>Articles Like</h1>} />
                  <Route path="/profile" element={<h1>Profile Page</h1>} />
                  <Route path="/subscription" element={<h1>Subscription Page</h1>} />
                  <Route path="/secretes" element={<h1>Subscription Articles[secretes]</h1>} />
                  <Route path="/subscription" element={<h1>Subscription Page</h1>} />
                  <Route path="*" element={<Navigate replace to="/" />} />
              </Routes>

              {/*<Footer />*/}
          </div>
      </>
  );
}

export default App;
