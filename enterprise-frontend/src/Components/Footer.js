import React from "react"

function Footer() {
  return (
    <footer className="bg-green-200 rounded-lg shadow w-full">
      <div className="mx-20 p-4 md:py-6">
        <div className="sm:flex sm:items-center sm:justify-between">
          <a href="#" className="flex justify-items-end mb-4 sm:mb-0 space-x-3">
            {/* Add any logo or content here */}
          </a>
          <ul className="flex flex-wrap items-center mb-6 text-sm font-medium text-gray-500 sm:mb-0">
            <li>
              <a href="#" className="hover:underline me-4 md:me-6">
                About
              </a>
            </li>
            <li>
              <a href="#" className="hover:underline me-4 md:me-6">
                Privacy Policy
              </a>
            </li>
            <li>
              <a href="#" className="hover:underline me-4 md:me-6">
                Licensing
              </a>
            </li>
            <li>
              <a href="#" className="hover:underline">
                Contact
              </a>
            </li>
          </ul>
        </div>
        <hr className="my-6 border-gray-200 sm:mx-auto" />
        <p className="text-sm text-center text-gray-500">
          &copy; {new Date().getFullYear()} Bookster. All Rights Reserved.
        </p>
      </div>
    </footer>
  )
}

export default Footer
