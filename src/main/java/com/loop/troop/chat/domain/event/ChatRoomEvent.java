package com.loop.troop.chat.domain.event;

import com.loop.troop.chat.domain.event.enums.ChatRoomEventType;

public interface ChatRoomEvent extends DomainEvent {

	ChatRoomEventType type();

	default String aggregateType() {
		return "ChatRoom";
	}

}