package com.example.bankledger.infrastructure.event;

import com.example.bankledger.domain.shared.DomainEvent;
import com.example.bankledger.domain.shared.DomainEventPublisher;

import java.util.List;
import java.util.logging.Logger;

/**
 * A trivial {@link DomainEventPublisher} that logs each event. In a real system this
 * would be replaced by an adapter that writes to an outbox table or a message broker.
 */
public class LoggingDomainEventPublisher implements DomainEventPublisher {

    private static final Logger LOG = Logger.getLogger(LoggingDomainEventPublisher.class.getName());

    @Override
    public void publish(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            LOG.info(() -> "Domain event: " + event);
        }
    }
}
