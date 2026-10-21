package com.example.bankledger.domain.shared;

import java.util.List;

/**
 * Port for emitting domain events to the outside world (logging, messaging, an
 * outbox, …). The application layer pulls events off an aggregate after saving it
 * and hands them to a publisher. Infrastructure supplies the implementation.
 */
public interface DomainEventPublisher {

    void publish(List<DomainEvent> events);
}
