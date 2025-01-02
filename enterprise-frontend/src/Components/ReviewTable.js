import React, { useState, useEffect } from "react"

const ReviewCards = () => {
  const [reviews, setReviews] = useState([])

  // Fetch reviews from the backend when the component mounts
  useEffect(() => {
    const fetchReviews = async () => {
      try {
        const response = await fetch("http://localhost:8080/getReviews", {
          method: "GET",
          headers: {
            "Content-Type": "application/json", // Optional: Set the content type if needed
          },
        })

        if (response.ok) {
          const data = await response.json()
          setReviews(data) // Set the reviews data into the state
        } else {
          console.error("Failed to fetch reviews")
        }
      } catch (error) {
        console.error("Error fetching reviews:", error)
      }
    }

    fetchReviews()
  }, []) // Empty dependency array means it runs only once when the component mounts

  const handleDeleteReview = async (reviewId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/deleteReview/${reviewId}`,
        {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("jwt")}`, // Add the JWT as a Bearer token
            "Content-Type": "application/json", // Optional: If needed by the server
          },
        }
      )

      if (response.ok) {
        // Filter out the deleted review from the state
        setReviews(reviews.filter((review) => review.review_id !== reviewId))
      } else {
        alert("You are not logged in, so you cannot delete reviews!")
      }
    } catch (error) {
      console.error("Error deleting the review:", error)
    }
  }

  const ReviewText = ({ text }) => {
    const [isExpanded, setIsExpanded] = useState(false)
    const MAX_LENGTH = 150

    const toggleText = () => {
      setIsExpanded(!isExpanded)
    }

    return (
      <p className="text-gray-700 mb-4">
        <span className="font-bold">Review:</span>{" "}
        {isExpanded ? text : `${text.slice(0, MAX_LENGTH)}...`}
        <button
          onClick={toggleText}
          className="text-pink-300 hover:underline ml-2"
          aria-label={isExpanded ? "Collapse review" : "Expand review"}
        >
          {isExpanded ? "Show Less" : "Read More"}
        </button>
      </p>
    )
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-6 text-center"></h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {reviews.map((review) => (
          <div
            key={review.review_id}
            className="bg-white shadow-md rounded-lg p-4 border border-gray-200 hover:shadow-lg transition-shadow duration-200"
            data-review-id={review.review_id}
          >
            {/* Reviewer Name */}
            <h2 className="text-lg font-semibold text-gray-800 mb-2">
              {review.reviewer_name}
            </h2>

            {/* Title */}
            <p className="text-gray-600 text-sm mb-2">
              <span className="font-bold">Title:</span> {review.title}
            </p>

            {/* Book Image */}
            <div className="flex justify-center items-center mb-4">
              <img
                src={review.image_url || "https://via.placeholder.com/150"}
                alt={`Cover for book ID ${review.book_id}`}
                loading="lazy"
                className="w-32 h-48 object-cover"
                onError={(e) =>
                  (e.target.src = "https://via.placeholder.com/150")
                }
              />
            </div>

            {/* Review Text */}
            <ReviewText text={review.review_text} />

            {/* Rating and Review Date */}
            <div className="flex items-center justify-between text-sm text-gray-500">
              <span>
                <span className="font-bold">Rating:</span> {review.rating}
              </span>
              <span>
                {review.review_date
                  ? new Date(review.review_date).toLocaleDateString()
                  : "Unknown Date"}
              </span>
            </div>

            {/* Delete Button */}
            <div className="flex justify-end mt-4">
              <button
                onClick={() => handleDeleteReview(review.review_id)} // Calls the handleDeleteReview function
                className="bg-green-300 text-white px-4 py-2 rounded hover:bg-red-600 transition-colors duration-200"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default ReviewCards
