package com.example.bankledger.application.command;

import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Money;

/** Request to withdraw money from an account. */
public record WithdrawCommand(AccountId accountId, Money amount, String description) {
}
