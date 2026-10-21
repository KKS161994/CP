package com.example.bankledger.domain.shared;

import java.time.Instant;

/**
 * Marker for something that happened in the domain that domain experts care about.
 * Events are immutable facts about the past, named in the past tense.
 */
public interface DomainEvent {

    /** When the event occurred. */
    Instant occurredOn();
}
