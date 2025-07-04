package com.librarymanagement.demo.domain.valueobjects;

import static java.util.Objects.requireNonNull;

public class Address {
    private final String street;
    private final String city;
    private final String country;
    private final String postalCode;

    public Address(String street, String city, String country, String postalCode) {
        this.street = requireNonNull(street);
        this.city = requireNonNull(city);
        this.country = requireNonNull(country);
        this.postalCode = requireNonNull(postalCode);
    }

    // getters, equals, hashCode
}
