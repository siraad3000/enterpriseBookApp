package se.systementor.enterpriseBookBackend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import se.systementor.enterpriseBookBackend.config.DatabaseConnection;

import java.sql.*;
import java.util.Iterator;

@Service
public class BookService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final DatabaseConnection dbConnection = new DatabaseConnection();
    private static final String API_KEY = "AIzaSyAbFb6wmsqvojf4qdfRsriJ4x0YKTMRBFY"; // Temporary API key placeholder

    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }



    public JsonNode searchBooks(String authors, String title, String category) {
        ArrayNode resultArray = objectMapper.createArrayNode();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM books WHERE 1=1"); // Base query

        // Append conditions based on non-null parameters
        if (authors != null && !authors.isEmpty()) {
            queryBuilder.append(" AND authors LIKE ?");
        }
        if (title != null && !title.isEmpty()) {
            queryBuilder.append(" AND title LIKE ?");
        }
        if (category != null && !category.isEmpty()) {
            queryBuilder.append(" AND categories LIKE ?");
        }

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;

            // Set parameters for the prepared statement
            if (authors != null && !authors.isEmpty()) {
                stmt.setString(paramIndex++, "%" + authors + "%");
            }
            if (title != null && !title.isEmpty()) {
                stmt.setString(paramIndex++, "%" + title + "%");
            }
            if (category != null && !category.isEmpty()) {
                stmt.setString(paramIndex++, "%" + category + "%");
            }

            ResultSet rs = stmt.executeQuery();

            // Process the result set
            while (rs.next()) {
                ObjectNode bookJson = objectMapper.createObjectNode();
                bookJson.put("id", rs.getInt("id"));
                bookJson.put("title", rs.getString("title"));
                bookJson.put("authors", rs.getString("authors"));
                bookJson.put("isbn", rs.getString("isbn"));
                bookJson.put("publisher", rs.getString("publisher"));
                bookJson.put("publishedDate", rs.getString("publishedDate"));
                bookJson.put("description", rs.getString("description"));
                bookJson.put("categories", rs.getString("categories"));
                bookJson.put("pageCount", rs.getInt("pageCount"));
                bookJson.put("image_url", rs.getString("image_url"));


                resultArray.add(bookJson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultArray;
    }

    public JsonNode searchReviews(String reviewerName) {
        ArrayNode resultArray = objectMapper.createArrayNode();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM book_reviews br JOIN books b ON br.book_id = b.id WHERE 1=1"); // Modified query with JOIN

        // Append conditions based on non-null parameters
        if (reviewerName != null && !reviewerName.isEmpty()) {
            queryBuilder.append(" AND br.reviewer_name LIKE ?");
        }

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;

            // Set parameters for the prepared statement
            if (reviewerName != null && !reviewerName.isEmpty()) {
                stmt.setString(paramIndex++, "%" + reviewerName + "%");
            }

            ResultSet rs = stmt.executeQuery();

            // Process the result set
            while (rs.next()) {
                ObjectNode reviewJson = objectMapper.createObjectNode();
                reviewJson.put("review_id", rs.getInt("review_id"));
                reviewJson.put("book_id", rs.getString("book_id"));
                reviewJson.put("reviewer_name", rs.getString("reviewer_name"));
                reviewJson.put("review_text", rs.getString("review_text"));
                reviewJson.put("rating", rs.getInt("rating"));
                reviewJson.put("review_date", rs.getString("review_date"));
                reviewJson.put("published_date", rs.getString("publishedDate"));  // Include publishedDate
                reviewJson.put("image_url", rs.getString("image_url"));  // Include imageUrl
                reviewJson.put("title", rs.getString("title"));


                resultArray.add(reviewJson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultArray;
    }


    public JsonNode getUserLibrary(int userId) {
        String query = "SELECT title, image_url, publishedDate FROM saved_books WHERE user_id = ?";
        ArrayNode jsonArray = objectMapper.createArrayNode();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (conn == null){
                System.out.println("Failed to establish database connection");
                return jsonArray;
            }

            while (rs.next()) {
                ObjectNode bookJson = objectMapper.createObjectNode();
                bookJson.put("title", rs.getString("title"));
                bookJson.put("image_url", rs.getString("image_url"));
                bookJson.put("publishedDate", rs.getString("publishedDate"));
                jsonArray.add(bookJson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return jsonArray;
    }





    public JsonNode getAndLoadBooks() {
        String query = "SELECT * FROM books";
        ArrayNode jsonArray = objectMapper.createArrayNode();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ObjectNode userJson = objectMapper.createObjectNode();
                userJson.put("id", rs.getInt("id"));
                userJson.put("name", rs.getString("name"));
                userJson.put("email", rs.getString("email"));
                jsonArray.add(userJson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
    public void fetchAndStoreBooksFromGoogleAPI(String searchString) {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=" + searchString + "&key=" + API_KEY;
        JsonNode response = restTemplate.getForObject(apiUrl, JsonNode.class);
        System.out.println("API Response: " + response);

        if (response != null && response.has("items")) {
            ArrayNode items = (ArrayNode) response.get("items");

            try (Connection conn = dbConnection.getConnection()) {
                String insertQuery = "INSERT INTO books (title, authors, isbn, publisher, publishedDate, description, categories, pageCount, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

                // Debug: Count successful inserts
                int validBooksCount = 0;

                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.get("volumeInfo");

                    // Extract book details
                    String title = volumeInfo.path("title").asText("");
                    String authors = volumeInfo.path("authors").isArray() ? joinArray((ArrayNode) volumeInfo.path("authors")) : "";
                    String isbn = getISBN(volumeInfo);
                    String publisher = volumeInfo.path("publisher").asText("");
                    String publishedDate = volumeInfo.path("publishedDate").asText("");
                    String description = volumeInfo.path("description").asText("");
                    String categories = volumeInfo.path("categories").isArray() ? joinArray((ArrayNode) volumeInfo.path("categories")) : "";
                    int pageCount = volumeInfo.path("pageCount").asInt(0);

                    String image_url = "";
                    JsonNode imageLinks = volumeInfo.get("imageLinks");
                    if (imageLinks != null && imageLinks.has("thumbnail")) {
                        image_url = imageLinks.path("thumbnail").asText();
                    }

                    // Only process rows with valid ISBN
                    if (!isbn.isEmpty()) {
                        stmt.setString(1, title);
                        stmt.setString(2, authors);
                        stmt.setString(3, isbn);
                        stmt.setString(4, publisher);
                        stmt.setString(5, publishedDate);
                        stmt.setString(6, description);
                        stmt.setString(7, categories);
                        stmt.setInt(8, pageCount);
                        stmt.setString(9, image_url);

                        stmt.addBatch();
                        validBooksCount++;
                    } else {
                        System.out.println("Skipping book due to missing ISBN: " + title);
                    }
                }

                // Execute the batch insert
                if (validBooksCount > 0) {
                    int[] result = stmt.executeBatch();
                    System.out.println("Batch executed successfully. Rows inserted: " + result.length);

                    // Retrieve the generated IDs
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    while (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        System.out.println("Generated book ID: " + id);
                    }
                } else {
                    System.out.println("No valid books to insert.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while inserting books into the database", e);
            }
        } else {
            System.out.println("No books found for the search string: " + searchString);
        }
    }


    // Helper method to join an ArrayNode into a comma-separated string
    private String joinArray(ArrayNode arrayNode) {
        StringBuilder result = new StringBuilder();
        Iterator<JsonNode> elements = arrayNode.elements();
        while (elements.hasNext()) {
            result.append(elements.next().asText()).append(", ");
        }
        return result.length() > 2 ? result.substring(0, result.length() - 2) : result.toString();
    }

    // Helper method to extract ISBN from industryIdentifiers
    private String getISBN(JsonNode volumeInfo) {
        if (volumeInfo.has("industryIdentifiers")) {
            for (JsonNode identifier : volumeInfo.get("industryIdentifiers")) {
                if ("ISBN_13".equals(identifier.path("type").asText())) {
                    return identifier.path("identifier").asText();
                }
            }
        }
        return "";
    }

    public boolean postReview(String reviewerName, String reviewText, int rating, int bookId) {
        String query = "SELECT publishedDate, image_url, title FROM books WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Retrieve book details (publishedDate and image_url)
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String publishedDate = rs.getString("publishedDate");
                String image_url = rs.getString("image_url");
                String bookTitle = rs.getString("title");



                // Now, insert the review into the book_reviews table, including the new fields
                String insertQuery = "INSERT INTO book_reviews (book_id, reviewer_name, review_text, rating, publishedDate, image_url, title) VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, bookId);
                    insertStmt.setString(2, reviewerName);
                    insertStmt.setString(3, reviewText);
                    insertStmt.setInt(4, rating);
                    insertStmt.setString(5, publishedDate);  // Use the publishedDate from the books table
                    insertStmt.setString(6, image_url);  // Use the imageUrl from the books table
                    insertStmt.setString(7, bookTitle);

                    int rowsAffected = insertStmt.executeUpdate();
                    return rowsAffected > 0;  // Returns true if the insert was successful
                }
            } else {
                return false;  // If no book is found for the given bookId
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Returns false if an error occurred
        }
    }

    public static boolean saveBookForUser(int userId, int Id, String title, String imageUrl, String publishedDate) {
        String insertQuery = "INSERT INTO saved_books (user_id, id, title, image_url, publishedDate) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, Id);
            stmt.setString(3, title);
            stmt.setString(4, imageUrl);
            stmt.setString(5, publishedDate);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the insert was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an error occurred
        }

    }

    public static void main(String[] args) {
        int userId = 1; // Example user ID
        int bookId = 101; // Example book ID
        String title = "The Great Gatsby";
        String imageUrl = "https://example.com/image.jpg";
        String publishedDate = "1925-04-10";

        boolean success = saveBookForUser(userId, bookId, title, imageUrl, publishedDate);
        if (success) {
            System.out.println("Book saved successfully!");
        } else {
            System.out.println("Failed to save the book. Please check the input values.");
        }
    }




    // Method to delete a book by its ID
    public boolean deleteBookById(int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;  // Return true if at least one row was deleted

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a review by its ID
    public boolean deleteReviewById(int id) {
        String query = "DELETE FROM book_reviews WHERE review_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;  // Return true if at least one row was deleted

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
