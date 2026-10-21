package com.example.bankledger.domain.model.ledger;

/**
 * Direction of a ledger entry. In double-entry terms, a CREDIT increases the
 * account balance and a DEBIT decreases it (from the account holder's perspective).
 */
public enum EntryType {
    CREDIT,
    DEBIT
}
