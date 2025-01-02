import React from "react"

const AboutUsPage = () => {
  return (
    <div className="max-w-4xl mx-auto p-6 bg-gradient-to-br from-green-100 via-white to-green-50 shadow-lg rounded-lg">
      <div className="text-center mb-8">
        <h1 className="text-4xl font-bold text-gray-800">About Us</h1>
        <p className="text-gray-600 mt-2">
          Discover what makes Bookster special.
        </p>
      </div>

      <div className="mb-8">
        <p className="text-gray-700 leading-relaxed mb-4">
          Welcome to <strong className="font-semibold">Bookster</strong>! We are
          a vibrant platform for book lovers where you can explore insightful
          reviews and share your own thoughts on your favorite books. Whether
          you’re a casual reader or a dedicated bibliophile, Bookster is the
          perfect place to connect and engage with a community that shares your
          passion for stories.
        </p>
        <p className="text-gray-700 leading-relaxed mb-4">
          By logging in, you can create your own personalized library to keep
          track of books you love, want to read, or have reviewed. It's your
          space to organize your literary adventures and share your journey with
          others.
        </p>
        <p className="text-gray-700 leading-relaxed">
          Thank you for joining us on this exciting journey of exploration and
          connection through books. At Bookster, every story matters, and we’re
          thrilled to help you discover and share yours!
        </p>
      </div>
    </div>
  )
}

export default AboutUsPage
