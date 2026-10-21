package com.example.bankledger.domain.service;

import com.example.bankledger.domain.model.account.Account;
import com.example.bankledger.domain.model.money.Money;

import java.util.Objects;

/**
 * A domain service for the one operation that does not belong to a single aggregate:
 * moving money between two accounts. It coordinates a debit on the source and a credit
 * on the destination, keeping both halves consistent and using a shared description.
 *
 * <p>It is stateless and side-effect-free with respect to storage — the application
 * layer is responsible for loading and saving the aggregates around this call.
 */
public class TransferService {

    public void transfer(Account from, Account to, Money amount, String reference) {
        Objects.requireNonNull(from, "source account must not be null");
        Objects.requireNonNull(to, "destination account must not be null");
        Objects.requireNonNull(amount, "amount must not be null");

        if (from.id().equals(to.id())) {
            throw new IllegalArgumentException("cannot transfer to the same account");
        }

        String ref = (reference == null || reference.isBlank()) ? "transfer" : reference;
        // Debit first so an insufficient-funds failure aborts before any money is credited.
        from.withdraw(amount, "Transfer to %s: %s".formatted(to.id(), ref));
        to.deposit(amount, "Transfer from %s: %s".formatted(from.id(), ref));
    }
}
