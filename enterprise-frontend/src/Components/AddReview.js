import React, { useState } from "react"

const handlePostReview = async (e, bookId) => {
    e.preventDefault();
  
    try {
      const response = await axios.post("http://localhost:8080/postReview", {
        bookId: bookId,
        reviewerName: reviewData.reviewerName,
        reviewText: reviewData.reviewText,
        rating: parseInt(reviewData.rating),
      });
      alert(response.data); // Show success message
      setShowReviewForm(null); 
      setReviewData({ reviewerName: "", reviewText: "", rating: "" }); 
    } catch (error) {
      console.error("Error posting review:", error);
      alert("Failed to post review");
    }
  };

  
  
