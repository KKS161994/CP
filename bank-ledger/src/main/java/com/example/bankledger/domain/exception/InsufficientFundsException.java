package com.example.bankledger.domain.exception;

import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Money;

/**
 * Raised when a withdrawal or transfer would push an account below zero.
 * This protects the core invariant of the Account aggregate: the balance is never negative.
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(AccountId accountId, Money balance, Money requested) {
        super("Account %s has insufficient funds: balance %s, requested %s"
                .formatted(accountId, balance, requested));
    }
}
