package se.systementor.enterpriseBookBackend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.systementor.enterpriseBookBackend.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

@Service
public class BookService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
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
                bookJson.put("title", rs.getString("title"));
                bookJson.put("authors", rs.getString("authors"));
                bookJson.put("isbn", rs.getString("isbn"));
                bookJson.put("publisher", rs.getString("publisher"));
                bookJson.put("publishedDate", rs.getString("publishedDate"));
                bookJson.put("description", rs.getString("description"));
                bookJson.put("categories", rs.getString("categories"));
                bookJson.put("pageCount", rs.getInt("pageCount"));

                resultArray.add(bookJson);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultArray;
    }

    public JsonNode getAndLoadBooks() {
        String query = "SELECT * FROM users";
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

    public void fetchAndStoreBooksFromGoogleAPI() {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=George+orwell&key=" + API_KEY;
        JsonNode response = restTemplate.getForObject(apiUrl, JsonNode.class);

        if (response != null && response.has("items")) {
            ArrayNode items = (ArrayNode) response.get("items");

            try (Connection conn = dbConnection.getConnection()) {
                String insertQuery = "INSERT INTO books (title, authors, isbn, publisher, publishedDate, description, categories, pageCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertQuery);

                for (JsonNode item : items) {
                    JsonNode volumeInfo = item.get("volumeInfo");

                    String title = volumeInfo.path("title").asText();
                    String authors = volumeInfo.path("authors").isArray() ? joinArray((ArrayNode) volumeInfo.path("authors")) : "";
                    String isbn = getISBN(volumeInfo);
                    String publisher = volumeInfo.path("publisher").asText("");
                    String publishedDate = volumeInfo.path("publishedDate").asText("");
                    String description = volumeInfo.path("description").asText("");
                    String categories = volumeInfo.path("categories").isArray() ? joinArray((ArrayNode) volumeInfo.path("categories")) : "";
                    int pageCount = volumeInfo.path("pageCount").asInt(0);

                    if (!isbn.isEmpty()) {
                        stmt.setString(1, title);
                        stmt.setString(2, authors);
                        stmt.setString(3, isbn);
                        stmt.setString(4, publisher);
                        stmt.setString(5, publishedDate);
                        stmt.setString(6, description);
                        stmt.setString(7, categories);
                        stmt.setInt(8, pageCount);
                        stmt.addBatch(); // Add to batch only if ISBN is valid
                    }

                }

                stmt.executeBatch(); // Execute the batch of insert statements

            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        String query = "INSERT INTO book_reviews (book_id, reviewer_name, review_text, rating) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookId);
            stmt.setString(2, reviewerName);
            stmt.setString(3, reviewText);
            stmt.setInt(4, rating);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;  // Returns true if the insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Returns false if an error occurred
        }
    }
    public boolean updateReviewText(int id, String reviewText) {
        String query = "UPDATE book_reviews SET review_text = ? WHERE book_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, reviewText);
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;  // Return true if at least one row was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
