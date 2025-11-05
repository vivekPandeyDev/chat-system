package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.exception.BusinessException;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

@Slf4j
public final class SingleChatRoom extends ChatRoom {

	public SingleChatRoom(@NotNull String roomId, @NotNull User createdBy, @NotNull User other) {
		this(roomId, createdBy, other, List.of());
	}

	public SingleChatRoom(@NotNull String roomId, @NotNull User createdBy, @NotNull User other,
			List<ChatRoomObserver> observers) {

		super(roomId, other.getUsername(), RoomType.SINGLE, createdBy);

		validateParticipants(createdBy, other);

		addParticipant(createdBy);
		addParticipant(other);

		if (observers != null && !observers.isEmpty()) {
			observers.forEach(this::addObserver);
		}

		log.info("SingleChatRoom created between '{}' and '{}'", createdBy.getUsername(), other.getUsername());
	}

	private static void validateParticipants(@NotNull User createdBy, @NotNull User other) {
		if (Objects.equals(createdBy.getUserId(), other.getUserId())) {
			throw new BusinessException("SAME_PARTICIPANT", "Single chat room must be between two distinct users",
					HttpStatus.BAD_REQUEST);
		}
	}

}