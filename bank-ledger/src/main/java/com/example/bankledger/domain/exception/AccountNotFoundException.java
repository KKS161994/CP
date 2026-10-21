package com.example.bankledger.domain.exception;

import com.example.bankledger.domain.model.account.AccountId;

/**
 * Raised by application services when a referenced account does not exist.
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(AccountId accountId) {
        super("Account not found: " + accountId);
    }
}
