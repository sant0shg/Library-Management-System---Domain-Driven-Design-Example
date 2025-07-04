package com.librarymanagement.demo.domain.repositories;

import com.librarymanagement.demo.domain.aggregates.Author;
import com.librarymanagement.demo.domain.valueobjects.AuthorId;
import com.librarymanagement.demo.domain.valueobjects.AuthorStatus;

import java.util.List;

public interface AuthorRepository {
    Author findById(AuthorId id);
    void save(Author author);
    List<Author> findByStatus(AuthorStatus status);
    List<Author> findByName(String firstName, String lastName);
}
