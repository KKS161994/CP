package com.example.bankledger.application.command;

import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Money;

/** Request to deposit money into an account. */
public record DepositCommand(AccountId accountId, Money amount, String description) {
}
