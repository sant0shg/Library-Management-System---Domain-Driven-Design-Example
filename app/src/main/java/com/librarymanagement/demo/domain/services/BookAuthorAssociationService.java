package com.librarymanagement.demo.domain.services;

import com.librarymanagement.demo.domain.entities.BookAuthorAssociation;
import com.librarymanagement.demo.domain.repositories.BookAuthorAssociationRepository;
import com.librarymanagement.demo.domain.valueobjects.AuthorId;
import com.librarymanagement.demo.domain.valueobjects.AuthorRole;
import com.librarymanagement.demo.domain.valueobjects.BookId;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class BookAuthorAssociationService {
    private final BookAuthorAssociationRepository associationRepository;

    public BookAuthorAssociationService(BookAuthorAssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }

    public void associateAuthorToBook(BookId bookId, AuthorId authorId, AuthorRole role,
                                      Integer contributionPercentage, Integer authorOrder, String associatedBy) {
        // Business rules for association creation
        if (associationRepository.existsActiveAssociation(bookId, authorId)) {
            throw new IllegalStateException("Author is already associated with this book");
        }

        // Check if there's already a primary author
        if (role == AuthorRole.PRIMARY_AUTHOR) {
            List<BookAuthorAssociation> existingAssociations = associationRepository.findActiveAssociationsByBookId(bookId);
            boolean hasPrimaryAuthor = existingAssociations.stream()
                    .anyMatch(BookAuthorAssociation::isPrimaryAuthor);
            if (hasPrimaryAuthor) {
                throw new IllegalStateException("Book already has a primary author");
            }
        }

        // Validate author order uniqueness
        if (authorOrder != null) {
            boolean orderExists = associationRepository.findActiveAssociationsByBookId(bookId)
                    .stream()
                    .anyMatch(assoc -> Objects.equals(assoc.getAuthorOrder(), authorOrder));
            if (orderExists) {
                throw new IllegalStateException("Author order " + authorOrder + " already exists for this book");
            }
        }

        BookAuthorAssociation association = new BookAuthorAssociation(
                bookId, authorId, role, contributionPercentage, authorOrder, associatedBy);
        associationRepository.save(association);
    }

    public void disassociateAuthorFromBook(BookId bookId, AuthorId authorId, String requestedBy) {
        BookAuthorAssociation association = associationRepository.findActiveAssociation(bookId, authorId);
        if (association == null) {
            throw new IllegalStateException("No active association found between book and author");
        }

        // Business rule: Cannot remove the last author
        List<BookAuthorAssociation> activeAssociations = associationRepository.findActiveAssociationsByBookId(bookId);
        if (activeAssociations.size() <= 1) {
            throw new IllegalStateException("Cannot remove the last author from a book");
        }

        association.deactivate();
        associationRepository.save(association);
    }

    public List<AuthorId> findAuthorsForBook(BookId bookId) {
        return associationRepository.findActiveAssociationsByBookId(bookId)
                .stream()
                .sorted(Comparator.comparing(assoc -> assoc.getAuthorOrder() != null ? assoc.getAuthorOrder() : Integer.MAX_VALUE))
                .map(BookAuthorAssociation::getAuthorId)
                .collect(Collectors.toList());
    }

    public List<BookId> findBooksForAuthor(AuthorId authorId) {
        return associationRepository.findActiveAssociationsByAuthorId(authorId)
                .stream()
                .map(BookAuthorAssociation::getBookId)
                .collect(Collectors.toList());
    }

    public AuthorId findPrimaryAuthor(BookId bookId) {
        return associationRepository.findActiveAssociationsByBookId(bookId)
                .stream()
                .filter(BookAuthorAssociation::isPrimaryAuthor)
                .map(BookAuthorAssociation::getAuthorId)
                .findFirst()
                .orElse(null);
    }

    public void validateTotalContribution(BookId bookId) {
        List<BookAuthorAssociation> associations = associationRepository.findActiveAssociationsByBookId(bookId);

        int totalContribution = associations.stream()
                .filter(assoc -> assoc.getContributionPercentage() != null)
                .mapToInt(BookAuthorAssociation::getContributionPercentage)
                .sum();

        if (totalContribution > 100) {
            throw new IllegalStateException("Total contribution percentage cannot exceed 100%");
        }
    }

    public List<BookAuthorAssociation> findCollaborativeBooks(AuthorId authorId) {
        return associationRepository.findActiveAssociationsByAuthorId(authorId)
                .stream()
                .filter(assoc -> {
                    List<BookAuthorAssociation> bookAssociations = associationRepository.findActiveAssociationsByBookId(assoc.getBookId());
                    return bookAssociations.size() > 1; // More than one author
                })
                .collect(Collectors.toList());
    }
}
