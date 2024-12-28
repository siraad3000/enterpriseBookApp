import React from "react"

const BookTable = ({ books }) => {
  return (
    <div className="container mx-auto p-4">
     

      {/* Table */}
      <table className="table-auto w-full border-collapse border border-gray-300">
        <thead>
          <tr className="bg-gray-100">
            <th className="border border-gray-300 px-4 py-2 text-red-500 font-bold">
              ISBN
            </th>
            <th className="border border-gray-300 px-4 py-2">Title</th>
            <th className="border border-gray-300 px-4 py-2">Authors</th>
            <th className="border border-gray-300 px-4 py-2">Published Year</th>
            <th className="border border-gray-300 px-4 py-2">Description</th>
          </tr>
        </thead>
        <tbody>
          {books.map((book) => (
            <tr key={book.id} className="hover:bg-gray-50">
              <td className="border border-gray-300 px-4 py-2">{book.isbn}</td>
              <td className="border border-gray-300 px-4 py-2">{book.title}</td>
              <td className="border border-gray-300 px-4 py-2">
                {book.authors}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {book.publishedDate}
              </td>
              <td className="border border-gray-300 px-4 py-2">
                {book.description}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default BookTable
