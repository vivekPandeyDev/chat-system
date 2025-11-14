package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.event.ChatRoomEvent;
import com.loop.troop.chat.domain.event.room.ChatRoomCreatedEvent;
import com.loop.troop.chat.domain.event.room.ChatRoomMessageSendEvent;
import com.loop.troop.chat.domain.observer.ObservableDomain;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Slf4j
public abstract sealed class ChatRoom extends ObservableDomain<ChatRoomEvent> permits SingleChatRoom, GroupChatRoom {

	protected final String roomId;

	protected final String roomName;

	protected final RoomType type;

	protected final User createdBy;

	protected final LocalDateTime createdAt;

	protected boolean isActive;

	private String imagePath;

	private final List<User> participants = new ArrayList<>();

	protected ChatRoom(String roomId, String roomName, RoomType type, User createdBy) {
		this.roomId = roomId;
		this.type = type;
		this.createdBy = createdBy;
		this.roomName = roomName;
		this.createdAt = LocalDateTime.now();
		this.isActive = true;
		recordEvent(new ChatRoomCreatedEvent(roomId, roomName, createdBy));
	}

	public void addParticipant(User user) {
		Objects.requireNonNull(user, "Participant cannot be null");
		if (!participants.contains(user)) {
			participants.add(user);
		}
	}

	public void removeParticipant(User user) {
		participants.remove(user);
	}

	public List<User> getParticipants() {
		return List.copyOf(participants);
	}

	public void deactivate() {
		this.isActive = false;
	}

	public void sendMessage(Message msg) {
		var event = new ChatRoomMessageSendEvent(msg.getMessageId(), this.roomName, msg, this.createdBy);
		recordEvent(event);
		log.info("[ChatRoom:{}] Message event created: {}", roomId, event);

	}

}