import React, { useEffect, useState } from "react"
import ReviewTable from "../Components/ReviewTable"

const ReviewsPage = () => {
  const [reviews, setReviews] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")

  useEffect(() => {
    // Retrieve the JWT from session storage

    // Fetch reviews with the JWT in the Authorization header
    fetch("http://localhost:8080/getReviews", {
      method: "GET",
      headers: {
        "Content-Type": "application/json", // Optional: Add content type header if needed
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch reviews")
        }
        return response.json()
      })
      .then((data) => {
        setReviews(data)
        setLoading(false)
      })
      .catch((err) => {
        setError(err.message)
        setLoading(false)
      })
  }, [])

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <p className="text-lg font-medium">Loading reviews...</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-screen">
        <p className="text-lg font-medium text-red-500">{error}</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold text-center mb-6"></h1>
      <ReviewTable reviews={reviews} />
    </div>
  )
}

export default ReviewsPage
