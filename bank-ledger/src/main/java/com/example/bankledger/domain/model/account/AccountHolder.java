package com.example.bankledger.domain.model.account;

import java.util.Objects;

/**
 * The owner of an account, modeled as a value object. In a richer system this might
 * reference a Customer aggregate by id; here it captures just enough to be meaningful.
 */
public record AccountHolder(String name) {

    public AccountHolder {
        Objects.requireNonNull(name, "holder name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("holder name must not be blank");
        }
    }
}
