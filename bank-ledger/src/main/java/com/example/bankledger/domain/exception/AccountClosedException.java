package com.example.bankledger.domain.exception;

import com.example.bankledger.domain.model.account.AccountId;

/**
 * Raised when a movement is attempted on a closed account.
 */
public class AccountClosedException extends RuntimeException {

    public AccountClosedException(AccountId accountId) {
        super("Account " + accountId + " is closed and cannot accept movements");
    }
}
