package com.loop.troop.chat.domain.event.room;

import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.domain.User;
import com.loop.troop.chat.domain.event.ChatRoomEvent;
import com.loop.troop.chat.domain.event.enums.ChatRoomEventType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatRoomMessageSendEvent (UUID eventId, ChatRoomEventType type, String aggregateId, String roomName,
                                        User createdBy, Message message,
                                        LocalDateTime occurredOn) implements ChatRoomEvent {

    public ChatRoomMessageSendEvent(String aggregateId, String roomName,Message message, User createdBy) {
        this(UUID.randomUUID(), ChatRoomEventType.MESSAGE_SENT, aggregateId, roomName, createdBy,message, LocalDateTime.now());
    }

    @Override
    public String payload() {
        return message.getContent();
    }
}
