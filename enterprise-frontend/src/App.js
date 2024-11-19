import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './Components/LoginPage';
import BooksPage from './Pages/BooksPage';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/books" element={<BooksPage />} />
      </Routes>
    </Router>
  );
};

export default App;
