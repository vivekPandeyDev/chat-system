package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.service.ChatRoomObserver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SingleChatRoom extends ChatRoom {

	public SingleChatRoom(String roomId, @NotNull User createdBy, @NotNull User other) {
		super(roomId, RoomType.SINGLE, createdBy);
		addParticipant(createdBy);
		addParticipant(other);
	}

	public SingleChatRoom(String roomId, @NotNull User createdBy, @NotNull User other,
			List<ChatRoomObserver> chatRoomObservers) {
		super(roomId, RoomType.SINGLE, createdBy);
		addParticipant(createdBy);
		addParticipant(other);
		chatRoomObservers.forEach(this::addObserver);
	}

}