import React from "react"

const AboutUsPage = () => {
  return (
    <div className="max-w-4xl mx-auto p-6 bg-white shadow-md rounded-md">
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

      <div className="mt-8">
        <h3 className="text-2xl font-semibold text-gray-800 mb-4">
          Why Choose Bookster?
        </h3>
        <ul className="list-disc pl-6 text-gray-700 space-y-2">
          <li>Read and write authentic reviews for a wide range of books.</li>
          <li>
            Build your own personalized library to organize your reading
            journey.
          </li>
          <li>Connect with a community of passionate book lovers.</li>
          <li>Discover new stories and share your favorites.</li>
        </ul>
      </div>
    </div>
  )
}

export default AboutUsPage
