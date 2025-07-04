package com.librarymanagement.demo.domain.repositories;

import com.librarymanagement.demo.domain.valueobjects.BookId;
import com.librarymanagement.demo.domain.valueobjects.BookStatus;

import java.awt.print.Book;
import java.util.List;

public interface BookRepository {
    Book findById(BookId id);
    void save(Book book);
    List<Book> findByStatus(BookStatus status);
    List<Book> findByPublisher(String publisher);
}
