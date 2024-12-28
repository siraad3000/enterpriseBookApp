import React from "react"
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import LoginPage from "./Components/LoginPage"
import SiraadPage from "./Components/SiraadPage"
import Header from "./Components/Header" // Import Header
import BooksPage from "./Pages/BooksPage"
import AboutUsPage from "./Pages/AboutUsPage"

import ReviewsPage from "./Pages/ReviewsPage"
import SearchBar from "./Components/SearchBar"
import Footer from "./Components/Footer"

const App = () => {
  return (
    <Router>
      {/* Header will appear on all pages */}
      <Header />

      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/siraad" element={<SiraadPage />} />
        <Route path="/books" element={<BooksPage />} />
        <Route path="/AllReviews" element={<ReviewsPage />} />
        <Route path="/searchBooks" element={<SearchBar />} />
        <Route path="/About-us" element={<AboutUsPage />} />
      </Routes>

      <Footer />
    </Router>
  )
}

export default App
