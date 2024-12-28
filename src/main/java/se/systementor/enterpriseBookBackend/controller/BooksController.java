package se.systementor.enterpriseBookBackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import se.systementor.enterpriseBookBackend.models.ReviewRequest;
import se.systementor.enterpriseBookBackend.models.UpdateReviewTextRequest;
import se.systementor.enterpriseBookBackend.models.saveBookRequest;
import se.systementor.enterpriseBookBackend.services.BookService;
import se.systementor.enterpriseBookBackend.services.UserService;

import java.awt.print.Book;
import java.util.List;


@RestController
public class BooksController {


    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/loadDatabase")
    public JsonNode getAndLoadBooksService(@RequestParam String searchString) {
        bookService.fetchAndStoreBooksFromGoogleAPI(searchString);
        return bookService.getAndLoadBooks();
    }
    @GetMapping("/getReviews")
    public JsonNode getReviews(@RequestParam(required = false) String reviewerName) {
        return bookService.searchReviews(reviewerName);
    }

    @GetMapping("/searchBooks")
    public JsonNode searchBooks(@RequestParam(required = false) String authors,@RequestParam(required = false) String title,@RequestParam(required = false) String category){
        return  bookService.searchBooks(authors,title,category);
    }

    @PostMapping("/postReview")
    public ResponseEntity<String> postReview(@RequestBody ReviewRequest reviewRequest) {
        boolean isCreated = bookService.postReview(
                reviewRequest.getReviewerName(),
                reviewRequest.getReviewText(),
                reviewRequest.getRating(),
                reviewRequest.getBookId()

        );

        if (isCreated) {
            return ResponseEntity.ok("Review created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create review.");
        }
    }


    @PostMapping("/saveBook")
    public ResponseEntity<String> saveBook(@RequestBody saveBookRequest saveBookRequest) {
        // Get the currently authenticated user
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = authenticatedUser.getUsername();

        // Retrieve the user details from the database using username or userId (custom logic)
        UserDetails userDetails = BookService.loadUserById(id);
        Integer userId = userDetails.getId(); // Assume userDetails has a method to get the user ID

        // Check if the book belongs to the logged-in user (you can use the userId in the bookRequest if it contains the user ID)
        if (saveBookRequest.getUserId().equals(userId)) {
            // Save the book to the database
            bookService.saveBook(saveBookRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Book saved successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to save this book.");
        }
    }





    @GetMapping("/myLibrary/{userId}")
    public ResponseEntity<JsonNode> getUserLibrary(@PathVariable int userId) {
        JsonNode userLibrary = bookService.getUserLibrary(userId);
        return ResponseEntity.ok(userLibrary);
    }


    /*
    @PutMapping("/updateReviewText")
    public ResponseEntity<String> updateReviewText(@RequestBody UpdateReviewTextRequest updateRequest) {
        boolean isUpdated = bookService.updateReviewText(updateRequest.getId(), updateRequest.getReviewText());

        if (isUpdated) {
            return ResponseEntity.ok("Review text updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update review text.");
        }
    }

     */


    // Delete book by ID
    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable int id) {
        boolean isDeleted = bookService.deleteBookById(id);

        if (isDeleted) {
            return ResponseEntity.ok("Book deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete book.");
        }
    }

    // Delete review by ID
    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable int id) {
        boolean isDeleted = bookService.deleteReviewById(id);

        if (isDeleted) {
            return ResponseEntity.ok("Review deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete review.");
        }
    }



}
