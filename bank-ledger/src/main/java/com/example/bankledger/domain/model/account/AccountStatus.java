package com.example.bankledger.domain.model.account;

/**
 * Lifecycle state of an account. A closed account rejects further movements.
 */
public enum AccountStatus {
    OPEN,
    CLOSED
}
