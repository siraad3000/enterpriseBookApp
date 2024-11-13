package se.systementor.enterpriseBookBackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.systementor.enterpriseBookBackend.models.ReviewRequest;
import se.systementor.enterpriseBookBackend.models.UpdateReviewTextRequest;
import se.systementor.enterpriseBookBackend.services.BookService;


@RestController
public class BooksController {

    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/loadDatabase")
    public JsonNode getAndLoadBooksService(@RequestParam String searchString) {
        System.out.println("hello controller");
        bookService.fetchAndStoreBooksFromGoogleAPI();
        return  bookService.getAndLoadBooks();
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
    @PutMapping("/updateReviewText")
    public ResponseEntity<String> updateReviewText(@RequestBody UpdateReviewTextRequest updateRequest) {
        boolean isUpdated = bookService.updateReviewText(updateRequest.getId(), updateRequest.getReviewText());

        if (isUpdated) {
            return ResponseEntity.ok("Review text updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update review text.");
        }
    }
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
