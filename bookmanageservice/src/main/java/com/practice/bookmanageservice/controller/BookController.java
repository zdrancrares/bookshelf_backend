package com.practice.bookmanageservice.controller;

import com.practice.bookmanageservice.dto.BookRequest;
import com.practice.bookmanageservice.dto.BookResponse;
import com.practice.bookmanageservice.service.BookService;
import com.practice.bookmanageservice.service.ExternalApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ExternalApiService externalApiService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/grouped-by-genre")
    public ResponseEntity<Map<String, List<BookResponse>>> getGroupedByGenre() {
        return ResponseEntity.ok(bookService.getBooksGroupedByGenre());
    }

    @GetMapping("/cheapest")
    public ResponseEntity<BookResponse> getCheapest() {
        return bookService.getCheapestBook()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/external/{title}")
    public ResponseEntity<String> fetchExternal(@PathVariable String title) {
        return ResponseEntity.ok(externalApiService.fetchBookInfoSync(title));
    }
}
