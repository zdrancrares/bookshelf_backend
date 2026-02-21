package com.practice.bookmanageservice.service;

import com.practice.bookmanageservice.dto.BookRequest;
import com.practice.bookmanageservice.dto.BookResponse;
import com.practice.bookmanageservice.entity.Book;
import com.practice.bookmanageservice.repository.BookRepository;
import com.practice.bookmanageservice.exception.BookNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    public BookResponse getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookResponse::from)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id %d".formatted(id)));
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        var book = Book.builder()
                .title(request.title())
                .author(request.author())
                .price(request.price())
                .genre(request.genre())
                .build();
        var saved = bookRepository.save(book);
        log.info("Created book with id: {}", saved.getId());
        return BookResponse.from(saved);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id %d".formatted(id)));
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPrice(request.price());
        book.setGenre(request.genre());
        return BookResponse.from(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public Map<String, List<BookResponse>> getBooksGroupedByGenre() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponse::from)
                .collect(Collectors.groupingBy(
                        b -> Optional.ofNullable(b.genre()).orElse("UNKNOWN")
                ));
    }

    public Optional<BookResponse> getCheapestBook() {
        return bookRepository.findAll()
                .stream()
                .min((a, b) -> Double.compare(
                        Optional.ofNullable(a.getPrice()).orElse(Double.MAX_VALUE),
                        Optional.ofNullable(b.getPrice()).orElse(Double.MAX_VALUE)
                ))
                .map(BookResponse::from);
    }
}
