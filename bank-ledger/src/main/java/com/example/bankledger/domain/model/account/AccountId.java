package com.example.bankledger.domain.model.account;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of an {@link Account} aggregate. A typed identifier (rather than a raw
 * String/UUID) prevents mixing up identifiers of different aggregates at compile time.
 */
public record AccountId(UUID value) {

    public AccountId {
        Objects.requireNonNull(value, "AccountId value must not be null");
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }

    public static AccountId of(String value) {
        return new AccountId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
