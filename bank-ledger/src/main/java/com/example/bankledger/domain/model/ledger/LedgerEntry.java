package com.example.bankledger.domain.model.ledger;

import com.example.bankledger.domain.model.money.Money;

import java.time.Instant;
import java.util.Objects;

/**
 * An immutable line in an account's ledger: a single movement of money with a
 * direction, a running balance captured at the time it was posted, and a description.
 *
 * <p>A {@code LedgerEntry} is an entity local to the {@code Account} aggregate. It is
 * never modified once posted — corrections are made by posting compensating entries,
 * exactly as a real ledger works.
 */
public final class LedgerEntry {

    private final EntryId id;
    private final EntryType type;
    private final Money amount;
    private final Money balanceAfter;
    private final String description;
    private final Instant postedAt;

    public LedgerEntry(EntryId id,
                       EntryType type,
                       Money amount,
                       Money balanceAfter,
                       String description,
                       Instant postedAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.balanceAfter = Objects.requireNonNull(balanceAfter, "balanceAfter must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.postedAt = Objects.requireNonNull(postedAt, "postedAt must not be null");
        if (!amount.isPositive()) {
            throw new IllegalArgumentException("ledger entry amount must be positive");
        }
    }

    public EntryId id() {
        return id;
    }

    public EntryType type() {
        return type;
    }

    public Money amount() {
        return amount;
    }

    public Money balanceAfter() {
        return balanceAfter;
    }

    public String description() {
        return description;
    }

    public Instant postedAt() {
        return postedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LedgerEntry that)) {
            return false;
        }
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "%s %s (%s) -> balance %s".formatted(type, amount, description, balanceAfter);
    }
}
