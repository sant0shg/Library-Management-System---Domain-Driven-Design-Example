package com.librarymanagement.demo.domain.entities;

import com.librarymanagement.demo.domain.valueobjects.AuthorId;
import com.librarymanagement.demo.domain.valueobjects.AuthorRole;
import com.librarymanagement.demo.domain.valueobjects.BookId;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Getter
public class BookAuthorAssociation {
    private final BookId bookId;
    private final AuthorId authorId;
    private final AuthorRole role;
    private final Integer contributionPercentage;
    private final Integer authorOrder;
    private final LocalDateTime associatedAt;
    private final String associatedBy;
    private boolean isActive;
    private String notes;

    public BookAuthorAssociation(BookId bookId, AuthorId authorId, AuthorRole role,
                                 Integer contributionPercentage, Integer authorOrder, String associatedBy) {
        this.bookId = requireNonNull(bookId);
        this.authorId = requireNonNull(authorId);
        this.role = requireNonNull(role);
        this.contributionPercentage = contributionPercentage;
        this.authorOrder = authorOrder;
        this.associatedBy = requireNonNull(associatedBy);
        this.associatedAt = LocalDateTime.now();
        this.isActive = true;

        validateAssociation();
    }

    public void addNotes(String notes) {
        this.notes = notes;
    }

    public void deactivate() {
        if (!isActive) {
            throw new IllegalStateException("Association is already inactive");
        }
        this.isActive = false;
    }

    public void reactivate() {
        if (isActive) {
            throw new IllegalStateException("Association is already active");
        }
        this.isActive = true;
    }

    public boolean isPrimaryAuthor() {
        return role == AuthorRole.PRIMARY_AUTHOR;
    }

    public boolean isCreatedBy(String userId) {
        return this.associatedBy.equals(userId);
    }

    public boolean hasMajorContribution() {
        return contributionPercentage != null && contributionPercentage >= 50;
    }

    public Duration getAssociationAge() {
        return Duration.between(associatedAt, LocalDateTime.now());
    }

    private void validateAssociation() {
        if (!isActive) {
            throw new IllegalStateException("Cannot modify inactive association");
        }
        if (!isActive) {
            throw new IllegalStateException("Cannot modify inactive association");
        }
        if (contributionPercentage != null && (contributionPercentage < 0 || contributionPercentage > 100)) {
            throw new IllegalArgumentException("Contribution percentage must be between 0 and 100");
        }
        if (authorOrder != null && authorOrder < 1) {
            throw new IllegalArgumentException("Author order must be positive");
        }
    }

}
