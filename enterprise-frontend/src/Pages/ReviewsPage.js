import React, { useEffect, useState } from "react"
import ReviewTable from "../Components/ReviewTable"

const ReviewsPage = () => {
  const [reviews, setReviews] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")

  useEffect(() => {
    fetch("http://localhost:8080/getReviews")
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
        <p className="text-lg font-medium text-red-500">Error: {error}</p>
      </div>
    )
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold text-center mb-6">Reviews</h1>
      <ReviewTable reviews={reviews} />
    </div>
  )
}

export default ReviewsPage
