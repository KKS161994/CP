package com.example.bankledger.domain.event;

import com.example.bankledger.domain.model.account.AccountHolder;
import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.model.money.Currency;
import com.example.bankledger.domain.shared.DomainEvent;

import java.time.Instant;

public record AccountOpened(AccountId accountId,
                            AccountHolder holder,
                            Currency currency,
                            Instant occurredOn) implements DomainEvent {
}
