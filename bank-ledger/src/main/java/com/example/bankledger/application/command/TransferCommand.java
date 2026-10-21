package com.example.bankledger.application.command;

import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Money;

/** Request to transfer money from one account to another. */
public record TransferCommand(AccountId fromAccountId,
                              AccountId toAccountId,
                              Money amount,
                              String reference) {
}
