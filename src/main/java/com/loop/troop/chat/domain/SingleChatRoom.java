package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.exception.BusinessException;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.List;

public final class SingleChatRoom extends ChatRoom {

	public SingleChatRoom(String roomId, @NotNull User createdBy, @NotNull User other) {
		super(roomId, RoomType.SINGLE, createdBy);
		if (createdBy.getUserId().equals(other.getUserId())) {
			throw new BusinessException("SAME_PARTICIPANT", "single message room should be b/w different participants",
					HttpStatus.BAD_REQUEST);
		}
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