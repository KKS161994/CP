package com.example.bankledger.application.view;

import com.example.bankledger.domain.model.ledger.LedgerEntry;

import java.time.Instant;

/**
 * A flat, read-only projection of a {@link LedgerEntry} for returning to callers,
 * keeping the domain entity from leaking out of the application boundary.
 */
public record LedgerEntryView(String type,
                              String amount,
                              String balanceAfter,
                              String description,
                              Instant postedAt) {

    public static LedgerEntryView from(LedgerEntry entry) {
        return new LedgerEntryView(
                entry.type().name(),
                entry.amount().toString(),
                entry.balanceAfter().toString(),
                entry.description(),
                entry.postedAt());
    }
}
