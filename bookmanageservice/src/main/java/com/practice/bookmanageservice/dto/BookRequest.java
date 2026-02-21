package com.practice.bookmanageservice.dto;

import com.practice.bookmanageservice.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BookRequest(
        @NotBlank(message = "Title is required")
        String title,
        @NotBlank(message = "Author is required")
        String author,

        @Positive(message = "Price must be positive")
        Double price,

        Book.Genre genre
) {}
