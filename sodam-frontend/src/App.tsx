import React, {useContext} from 'react';
import { Route, Routes, Navigate, Link} from "react-router-dom";
import Footer from './components/Footer';
import ThemeContext, {ThemeContextProvider} from "./context/ThemeContext";

function App() {
    const context = useContext(ThemeContext);

    return (
      <>
          <ThemeContextProvider>
              <ul>
                  <li><Link to="/">Home</Link></li>
                  <li><Link to="/articles">Articles</Link></li>
                  <li><Link to="/articles/new">Articles New</Link></li>
                  <li><Link to="/articles/edit">Articles New</Link></li>
              </ul>

              <Routes>
                  <Route path="/" element={<h1>Home</h1>} />
                  <Route path="/articles" element={<h1>Articles</h1>} />
                  <Route path="/articles/:id" element={<h1>Articles Detail</h1>} />
                  <Route path="/articles/new" element={<h1>Articles New Page</h1>} />
                  <Route path="/articles/edit/:id" element={<h1>Articles Edit Page</h1>} />
                  <Route path="/profile" element={<h1>Profile Page</h1>} />
                  <Route path="/subscription" element={<h1>Subscription Page</h1>} />
                  <Route path="*" element={<Navigate replace to="/" />} />
              </Routes>

              <Footer />
          </ThemeContextProvider>
      </>
  );
}

export default App;
