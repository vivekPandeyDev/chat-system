package com.loop.troop.chat.domain.event.room;


import com.loop.troop.chat.domain.User;
import com.loop.troop.chat.domain.event.ChatRoomEvent;
import com.loop.troop.chat.domain.event.enums.ChatRoomEventType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatRoomCreatedEvent(UUID eventId, ChatRoomEventType type, String aggregateId, String roomName,
                                   User createdBy, String payload,
                                   LocalDateTime occurredOn) implements ChatRoomEvent {

    public ChatRoomCreatedEvent(String aggregateId, String roomName, User createdBy) {
        this(UUID.randomUUID(), ChatRoomEventType.CHAT_ROOM_CREATED, aggregateId, roomName, createdBy, "Room created by " + createdBy, LocalDateTime.now());
    }
}
