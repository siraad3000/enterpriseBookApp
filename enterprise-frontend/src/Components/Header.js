import React, { useState } from "react"
import { useNavigate } from "react-router-dom" // Import useNavigate hook

function Header() {
  const [menuOpen, setMenuOpen] = useState(false)
  const navigate = useNavigate() // Initialize useNavigate hook

  const toggleMenu = () => {
    setMenuOpen(!menuOpen)
  }

  // Logout function to clear session and redirect
  const logout = () => {
    sessionStorage.removeItem("jwt") // Remove JWT from sessionStorage
    navigate("/") // Redirect to the root of the application
  }

  return (
    <div className="relative bg-green-200 p-10 rounded-lg shadow-md">
      {/* Main Header Content */}
      <div className="flex justify-between ">
        <h2 className="text-2xl font-bold text-green-800">Bookster</h2>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex  space-x-52 mr-96">
          <a href="/About-us" className="text-green-800 hover:text-green-900 ">
            About Us
          </a>
          <a href="/AllReviews" className="text-green-800 hover:text-green-900">
            Reviews
          </a>
          <a
            href="/searchBooks"
            className="text-green-800 hover:text-green-900 "
          >
            Search Books
          </a>
        </nav>
      </div>

      {/* Hamburger Menu */}
      <button
        onClick={toggleMenu}
        className="text-green-800 absolute top-10 right-0 w-28 z-10  hover:text-green-900 focus:outline-none "
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={2}
          stroke="currentColor"
          className="w-6 h-6"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M4 6h16M4 12h16M4 18h16"
          />
        </svg>
      </button>

      {/* Dropdown Menu */}
      {menuOpen && (
        <div className="absolute top-16 right-0 w-48 bg-white border border-green-300 rounded-lg shadow-lg z-10">
          <ul className="flex flex-col text-green-800 p-4">
            <li
              className="hover:bg-green-100 p-2 rounded cursor-pointer"
              onClick={logout} // Call the logout function on click
            >
              Log Out
            </li>
          </ul>
        </div>
      )}
    </div>
  )
}

export default Header
