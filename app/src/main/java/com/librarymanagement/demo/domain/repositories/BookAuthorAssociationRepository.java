package com.librarymanagement.demo.domain.repositories;

import com.librarymanagement.demo.domain.entities.BookAuthorAssociation;
import com.librarymanagement.demo.domain.valueobjects.AuthorId;
import com.librarymanagement.demo.domain.valueobjects.BookId;

import java.util.List;

public interface BookAuthorAssociationRepository {
    void save(BookAuthorAssociation association);
    BookAuthorAssociation findActiveAssociation(BookId bookId, AuthorId authorId);
    boolean existsActiveAssociation(BookId bookId, AuthorId authorId);
    List<BookAuthorAssociation> findActiveAssociationsByBookId(BookId bookId);
    List<BookAuthorAssociation> findActiveAssociationsByAuthorId(AuthorId authorId);
    List<BookAuthorAssociation> findAllAssociationsByBookId(BookId bookId);
    List<BookAuthorAssociation> findAllAssociationsByAuthorId(AuthorId authorId);
}
