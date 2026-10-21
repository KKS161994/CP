package com.example.bankledger.domain.event;

import com.example.bankledger.domain.model.account.AccountId;
import com.example.bankledger.domain.shared.DomainEvent;

import java.time.Instant;

public record AccountClosed(AccountId accountId,
                            Instant occurredOn) implements DomainEvent {
}
