import React from "react"
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import LoginPage from "./Components/LoginPage"
import LoginRegisterPage from "./Components/LoginRegisterPage"
import Header from "./Components/Header"
import AboutUsPage from "./Pages/AboutUsPage"
import ReviewsPage from "./Pages/ReviewsPage"
import SearchBar from "./Components/SearchBar"
import Footer from "./Components/Footer"

const App = () => {
  return (
    <div className="flex flex-col min-h-screen">
      <Router>
        {/* Header will appear on all pages */}
        <Header />

        {/* Main content area */}
        <main className="flex-grow">
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/LoginRegister" element={<LoginRegisterPage />} />
            <Route path="/AllReviews" element={<ReviewsPage />} />
            <Route path="/searchBooks" element={<SearchBar />} />
            <Route path="/About-us" element={<AboutUsPage />} />
          </Routes>
        </main>

        {/* Footer */}
        <Footer />
      </Router>
    </div>
  )
}

export default App
