import React, { useState } from "react"
import { useNavigate } from "react-router-dom"

const LoginPage = () => {
  const [isRegistering, setIsRegistering] = useState(false)
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [email, setEmail] = useState("")
  const [feedbackMessage, setFeedbackMessage] = useState("")
  const navigate = useNavigate()

  const handleLogin = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch("http://localhost:8080/api/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      })

      if (response.ok) {
        setFeedbackMessage("Login successful!")
        navigate("/books")
      } else {
        setFeedbackMessage("Login failed. Please check your credentials.")
      }
    } catch (error) {
      setFeedbackMessage("An error occurred. Please try again later.")
    }
  }

  const handleRegister = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch("http://localhost:8080/api/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password, email }),
      })

      if (response.ok) {
        setFeedbackMessage("Registration successful! You can now log in.")
        setIsRegistering(false) // Switch back to login mode
      } else {
        setFeedbackMessage("Registration failed. Please try again.")
      }
    } catch (error) {
      setFeedbackMessage("An error occurred. Please try again later.")
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-lg max-w-sm w-full">
        <h1 className="text-2xl font-bold text-gray-800 text-center mb-4">
          {isRegistering ? "Register" : "Login"}
        </h1>

        {feedbackMessage && (
          <p className="text-sm text-center mb-4 text-red-500">
            {feedbackMessage}
          </p>
        )}

        <form
          onSubmit={isRegistering ? handleRegister : handleLogin}
          className="space-y-4"
        >
          {/* Username Field */}
          <div>
            <label className="block text-gray-700 font-medium mb-1">
              Username
            </label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-200 focus:outline-none"
              placeholder="Enter your username"
            />
          </div>

          {/* Password Field */}
          <div>
            <label className="block text-gray-700 font-medium mb-1">
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-200 focus:outline-none"
              placeholder="Enter your password"
            />
          </div>

          {/* Email Field (for registration) */}
          {isRegistering && (
            <div>
              <label className="block text-gray-700 font-medium mb-1">
                Email
              </label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-200 focus:outline-none"
                placeholder="Enter your email"
              />
            </div>
          )}

          {/* Submit Button */}
          <button
            type="submit"
            className="w-full bg-green-200  hover:bg-pink-200 text-pink-400 font-semibold py-2 rounded-lg shadow focus:ring-2 focus:ring-blue-400 focus:outline-none"
          >
            {isRegistering ? "Register" : "Login"}
          </button>
        </form>

        {/* Toggle between Login and Register */}
        <button
          onClick={() => setIsRegistering(!isRegistering)}
          className="mt-4 text-sm text-pink-400 hover:underline block text-center"
        >
          {isRegistering
            ? "Already have an account? Login here"
            : "Don't have an account? Register here"}
        </button>
      </div>
    </div>
  )
}

export default LoginPage
