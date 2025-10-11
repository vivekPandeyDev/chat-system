package com.loop.troop.chat.domain.event;

import com.loop.troop.chat.domain.enums.EventType;
import com.loop.troop.chat.domain.Message;

import java.time.LocalDateTime;

public record ChatEvent(EventType type, String roomId, Message message, LocalDateTime timestamp) {
}