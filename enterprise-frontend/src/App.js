import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './Components/LoginPage';
import TemplateComponent from './Components/TemplateComponent';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/template" element={<TemplateComponent />} />
      </Routes>
    </Router>
  );
};

export default App;
