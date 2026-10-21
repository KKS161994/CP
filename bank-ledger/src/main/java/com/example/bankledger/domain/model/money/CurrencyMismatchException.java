package com.example.bankledger.domain.model.money;

/**
 * Thrown when an operation is attempted between two {@link Money} values of
 * different currencies. This is a domain invariant violation, not a recoverable
 * condition, hence an unchecked exception.
 */
public class CurrencyMismatchException extends RuntimeException {

    public CurrencyMismatchException(Currency expected, Currency actual) {
        super("Currency mismatch: expected " + expected.code() + " but was " + actual.code());
    }
}
