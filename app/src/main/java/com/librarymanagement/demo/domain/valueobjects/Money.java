package com.librarymanagement.demo.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Currency;

import static java.util.Objects.requireNonNull;

public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = requireNonNull(amount);
        this.currency = requireNonNull(currency);
    }

    // behavior methods, getters, equals, hashCode
}
