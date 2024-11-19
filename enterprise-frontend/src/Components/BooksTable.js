import React from "react";

const BookTable = ({ books }) => {
  return (
    <table
      style={{
        width: "100%",
        borderCollapse: "collapse",
        marginTop: "20px",
      }}
    >
      <thead>
        <tr>
          <th style={styles.header}>ISBN</th>
          <th style={styles.header}>Title</th>
          <th style={styles.header}>Authors</th>
          <th style={styles.header}>Published Year</th>
          <th style={styles.header}>Description</th>

        </tr>
      </thead>
      <tbody>
        {books.map((book) => (
          <tr key={book.id} style={styles.row}>
            <td style={styles.cell}>{book.isbn}</td>
            <td style={styles.cell}>{book.title}</td>
            <td style={styles.cell}>{book.authors}</td>
            <td style={styles.cell}>{book.publishedDate}</td>
            <td style={styles.cell}>{book.description}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

const styles = {
  header: {
    borderBottom: "2px solid #ddd",
    padding: "10px",
    textAlign: "left",
    backgroundColor: "#f4f4f4",
  },
  row: {
    borderBottom: "1px solid #ddd",
  },
  cell: {
    padding: "10px",
  },
};

export default BookTable;
