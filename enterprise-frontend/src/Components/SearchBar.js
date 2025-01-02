import React, { useState } from "react"
import axios from "axios"

function SearchBar() {
  const [query, setQuery] = useState("")
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(false)
  const [likedBooks, setLikedBooks] = useState({})
  const [showReviewForm, setShowReviewForm] = useState(null) 
  const [reviewData, setReviewData] = useState({
    reviewerName: "",
    reviewText: "",
    rating: "",
  })

  // Handle book search
  const handleSearch = async (event) => {
    event.preventDefault()
    setLoading(true)
    try {
      const response = await axios.get("http://localhost:8080/searchBooks", {
        params: { title: query },
      })
      setBooks(response.data)
      console.log("Hej")
      console.log(response.data)
    } catch (error) {
      console.error("Error fetching books:", error)
    } finally {
      setLoading(false)
    }
  }

  // Handle review submission
  const handlePostReview = async (event, bookId) => {
    event.preventDefault()
    try {
      const response = await axios.post(
        "http://localhost:8080/postReview",
        {
          reviewerName: reviewData.reviewerName,
          reviewText: reviewData.reviewText,
          rating: reviewData.rating,
          bookId: bookId, // Include the book ID
        },
        {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("jwt")}`, 
            "Content-Type": "application/json",
          },
        }
      )

      console.log(response)
      if (response.status === 200) {
        alert("Review submitted successfully!")
        setShowReviewForm(null)
        setReviewData({ reviewerName: "", reviewText: "", rating: "" })
      } else if (response.status === 401) {
        alert("You are not logged in!")
      } else {
        alert("Failed to submit review: " + response.data.message)
      }
    } catch (error) {
      console.error("Error posting review:", error)
      if (error.status === 401) {
        alert("You are not logged in!")
      } else {
        alert("an error ocurred!")
      }
    }
  }

  return (
    <div className="flex flex-col items-center p-6">
      {/* Search Form */}
      <form
        onSubmit={handleSearch}
        className="flex flex-col items-center w-full max-w-lg"
      >
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search for books"
          className="p-2 border border-green-500 rounded-lg mb-4 w-full"
        />
        <button
          type="submit"
          className="bg-pink-200 text-green-500 p-2 rounded-md w-full"
        >
          Search
        </button>
      </form>

      {/* Loading Indicator */}
      {loading && <p className="mt-4">Loading...</p>}

      {/* Book Grid */}
      {books.length > 0 && (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mt-6 w-full max-w-screen-lg">
          {books.map((book) => (
            <div
              key={book.id}
              className="border p-4 rounded-lg bg-white shadow-md hover:shadow-lg transition duration-300 ease-in-out"
            >
              <h3 className="text-xl font-semibold truncate">{book.title}</h3>
              <p className="text-gray-600 text-sm mb-2">By: {book.authors}</p>

              <div className="flex justify-center items-center">
                <img
                  src={book.image_url || "https://via.placeholder.com/150"}
                  alt={book.title}
                  loading="lazy"
                  className="w-32 h-48 object-cover"
                />
              </div>

              {/* Book Description */}
              <p className="text-gray-700 text-sm line-clamp-3 mb-4">
                {book.description}
              </p>

              {/* Published Date */}
              <p className="text-gray-600 text-sm mb-2">
                Published: {book.publishedDate}
              </p>

              {/* Title */}
              <p className="text-gray-600 text-sm mb-2">Title: {book.title}</p>

              {/* Add Review Button */}
              <button
                className="bg-pink-200 hover:bg-pink-300 text-pink-800 font-semibold py-2 px-4 rounded shadow"
                onClick={() => setShowReviewForm(book.id)}
              >
                Add Review
              </button>

              {/* Review Form */}
              {showReviewForm === book.id && (
                <form
                  className="mt-4 p-4 border rounded-lg bg-gray-100"
                  onSubmit={(e) => handlePostReview(e, book.id)}
                >
                  <input
                    type="text"
                    placeholder="Your Name"
                    value={reviewData.reviewerName}
                    onChange={(e) =>
                      setReviewData({
                        ...reviewData,
                        reviewerName: e.target.value,
                      })
                    }
                    className="p-2 border border-gray-300 rounded-lg mb-2 w-full"
                    required
                  />
                  <textarea
                    placeholder="Your Review"
                    value={reviewData.reviewText}
                    onChange={(e) =>
                      setReviewData({
                        ...reviewData,
                        reviewText: e.target.value,
                      })
                    }
                    className="p-2 border border-gray-300 rounded-lg mb-2 w-full"
                    required
                  />
                  <input
                    type="number"
                    placeholder="Rating (1-5)"
                    value={reviewData.rating}
                    onChange={(e) =>
                      setReviewData({
                        ...reviewData,
                        rating: e.target.value,
                      })
                    }
                    className="p-2 border border-gray-300 rounded-lg mb-4 w-full"
                    required
                    min="1"
                    max="5"
                  />
                  <div className="flex gap-2">
                    <button
                      type="submit"
                      className="bg-green-200 hover:bg-green-300 text-green-800 py-2 px-4 rounded"
                    >
                      Submit Review
                    </button>
                    <button
                      type="button"
                      onClick={() => setShowReviewForm(null)}
                      className="bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded"
                    >
                      Cancel
                    </button>
                  </div>
                </form>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default SearchBar
