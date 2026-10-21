package com.example.bankledger.domain.model.ledger;

import java.util.Objects;
import java.util.UUID;

/**
 * Identity of a {@link LedgerEntry}. Entries are local entities inside the Account
 * aggregate, so their identity only needs to be unique within that aggregate.
 */
public record EntryId(UUID value) {

    public EntryId {
        Objects.requireNonNull(value, "EntryId value must not be null");
    }

    public static EntryId generate() {
        return new EntryId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
