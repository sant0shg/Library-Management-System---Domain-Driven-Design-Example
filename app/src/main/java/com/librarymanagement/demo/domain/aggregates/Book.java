package com.librarymanagement.demo.domain.aggregates;

import com.librarymanagement.demo.domain.valueobjects.BookId;
import com.librarymanagement.demo.domain.valueobjects.BookStatus;
import com.librarymanagement.demo.domain.valueobjects.Isbn;
import com.librarymanagement.demo.domain.valueobjects.Money;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;


@Getter
class Book {
    private final BookId id;

    private final Isbn isbn;
    private String title;
    private String description;
    private Money price;
    private final LocalDate publishedDate;
    private final String publisher;
    private BookStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    // NO direct references to Author aggregate - only through association

    public Book(BookId id, Isbn isbn, String title, String description, Money price,
                LocalDate publishedDate, String publisher) {
        this.id = requireNonNull(id);
        this.isbn = requireNonNull(isbn);
        this.title = requireNonNull(title);
        this.description = requireNonNull(description);
        this.price = requireNonNull(price);
        this.publishedDate = requireNonNull(publishedDate);
        this.publisher = requireNonNull(publisher);
        this.status = BookStatus.AVAILABLE;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    // Business methods
    public void updateTitle(String newTitle) {
        if (this.status == BookStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot modify archived book");
        }
        this.title = requireNonNull(newTitle);
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void updatePrice(Money newPrice) {
        this.price = requireNonNull(newPrice);
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void markAsOutOfPrint() {
        this.status = BookStatus.OUT_OF_PRINT;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void archive() {
        this.status = BookStatus.ARCHIVED;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public boolean isAvailableForSale() {
        return this.status == BookStatus.AVAILABLE;
    }


}
