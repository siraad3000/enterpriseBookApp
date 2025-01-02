import React, { useState } from "react"
import { useNavigate } from "react-router-dom"
const LoginRegisterPage = () => {
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
        navigate("/About-us")
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

  return <h1>hello</h1>
}

export default LoginRegisterPage
