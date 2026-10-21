package com.example.bankledger.application.command;

import com.example.bankledger.domain.model.money.Currency;

/** Request to open a new account for a holder in a given currency. */
public record OpenAccountCommand(String holderName, Currency currency) {
}
