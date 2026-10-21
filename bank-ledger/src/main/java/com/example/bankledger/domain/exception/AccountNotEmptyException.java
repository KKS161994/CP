package com.example.bankledger.domain.exception;

import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Money;

/**
 * Raised when closing an account that still holds a non-zero balance.
 */
public class AccountNotEmptyException extends RuntimeException {

    public AccountNotEmptyException(AccountId accountId, Money balance) {
        super("Account %s cannot be closed with a non-zero balance of %s".formatted(accountId, balance));
    }
}
