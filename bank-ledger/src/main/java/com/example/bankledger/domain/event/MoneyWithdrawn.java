package com.example.bankledger.domain.event;

import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Money;
import com.example.bankledger.domain.shared.DomainEvent;

import java.time.Instant;

public record MoneyWithdrawn(AccountId accountId,
                             Money amount,
                             Money balanceAfter,
                             Instant occurredOn) implements DomainEvent {
}
