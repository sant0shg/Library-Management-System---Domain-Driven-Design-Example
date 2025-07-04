package com.librarymanagement.demo.domain.valueobjects;

public class Isbn {
    private final String value;

    public Isbn(String value) {
        if (!isValidISBN(value)) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        this.value = value;
    }

    private boolean isValidISBN(String isbn) {
        return isbn != null && isbn.matches("\\d{13}");
    }

    public String getValue() { return value; }

    // equals, hashCode, toString
}
