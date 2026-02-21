package com.practice.bookmanageservice.repository;

import com.practice.bookmanageservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorIgnoreCase(String author);

    @Query("SELECT b FROM Book b WHERE b.price <= :maxPrice ORDER BY b.price ASC")
    List<Book> findBooksUnderPrice(@Param("maxPrice") Double maxPrice);

    Optional<Book> findByTitleIgnoreCase(String title);

    @Query(value = "SELECT * FROM books WHERE genre = :genre", nativeQuery = true)
    List<Book> findByGenreNative(@Param("genre") String genre);
}
