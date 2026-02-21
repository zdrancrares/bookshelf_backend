package com.practice.bookmanageservice.dto;

import com.practice.bookmanageservice.entity.Book;

import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String author,
        Double price,
        String genre,
        LocalDateTime createdAt
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getGenre() != null ? book.getGenre().name() : null,
                book.getCreatedAt()
        );
    }
}
