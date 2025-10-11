package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.EventType;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.event.ChatEvent;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
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
public abstract sealed class ChatRoom permits SingleChatRoom, GroupChatRoom {

	protected final String roomId;

	protected final RoomType type;

	protected final User createdBy;

	protected final LocalDateTime createdAt;

	protected boolean isActive;

	protected final List<User> participants = new ArrayList<>();

	protected final List<ChatRoomObserver> observers = new ArrayList<>();

	protected ChatRoom(String roomId, RoomType type, User createdBy) {
		this.roomId = roomId;
		this.type = type;
		this.createdBy = createdBy;
		this.createdAt = LocalDateTime.now();
		this.isActive = true;
	}

	public void addParticipant(User user) {
		if (Objects.isNull(user)) {
			throw new IllegalArgumentException("Participant cannot be null in order to add in message room");
		}
		participants.add(user);
	}

	public void removeParticipant(User user) {
		participants.remove(user);
	}

	public List<User> getParticipants() {
		return List.copyOf(participants);
	}

	public void addObserver(ChatRoomObserver obs) {
		observers.add(obs);
	}

	public void removeObserver(ChatRoomObserver obs) {
		observers.remove(obs);
	}

	public void notifyObservers(ChatEvent event) {
		observers.forEach(o -> o.update(event));
	}

	public void sendMessage(Message msg) {
		var event = new ChatEvent(EventType.MESSAGE_SENT, roomId, msg, LocalDateTime.now());
		log.info("message send event created : {}", event);
		notifyObservers(event);
	}

}