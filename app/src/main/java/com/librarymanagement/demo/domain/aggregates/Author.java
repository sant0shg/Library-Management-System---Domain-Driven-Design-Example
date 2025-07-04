package com.librarymanagement.demo.domain.aggregates;

import com.librarymanagement.demo.domain.valueobjects.AuthorId;
import com.librarymanagement.demo.domain.valueobjects.AuthorStatus;
import com.librarymanagement.demo.domain.valueobjects.Address;
import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static java.util.Objects.requireNonNull;

@Getter
public class Author {
    private final AuthorId id;
    private String firstName;
    private String lastName;
    private final LocalDate birthDate;
    private String biography;
    private Address address;
    private String email;
    private AuthorStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    // NO direct references to Book aggregate - only through association

    public Author(AuthorId id, String firstName, String lastName, LocalDate birthDate,
                  String biography, Address address, String email) {
        this.id = requireNonNull(id);
        this.firstName = requireNonNull(firstName);
        this.lastName = requireNonNull(lastName);
        this.birthDate = requireNonNull(birthDate);
        this.biography = biography;
        this.address = address;
        this.email = requireNonNull(email);
        this.status = AuthorStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    // Business methods
    public void updateBiography(String newBiography) {
        this.biography = newBiography;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void updateAddress(Address newAddress) {
        this.address = newAddress;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void updateEmail(String newEmail) {
        if (!isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = newEmail;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void suspend() {
        this.status = AuthorStatus.SUSPENDED;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = AuthorStatus.ACTIVE;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public boolean isActive() {
        return this.status == AuthorStatus.ACTIVE;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

}
