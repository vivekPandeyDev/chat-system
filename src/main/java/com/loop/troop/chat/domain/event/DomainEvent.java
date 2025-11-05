package com.loop.troop.chat.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DomainEvent {
    /**
     * Unique identifier for this event instance.
     */
    UUID eventId();

    /**
     * The timestamp when the event occurred in the domain.
     */
    LocalDateTime occurredOn();

    /**
     * The name/type of the aggregate (e.g., "ChatRoom", "User").
     */
    String aggregateType();

    /**
     * The unique identifier of the aggregate that emitted this event.
     */
    String aggregateId();

    String payload();
}
