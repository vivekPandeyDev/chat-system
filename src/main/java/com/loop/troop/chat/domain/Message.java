package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.DeliveryStatus;
import com.loop.troop.chat.domain.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Message {

	private final String messageId;

	private final ChatRoom room;

	private final User sender;

	private final String content;

	private final MessageType type;

	private final LocalDateTime sentAt;

	private DeliveryStatus status;

	public Message(String messageId, ChatRoom room, User sender, String content, MessageType type) {
		this.messageId = messageId;
		this.room = room;
		this.sender = sender;
		this.content = content;
		this.type = type;
		this.sentAt = LocalDateTime.now();
		this.status = DeliveryStatus.SENT;
	}

	public void markDelivered() {
		this.status = DeliveryStatus.DELIVERED;
	}

	public void markSeen() {
		this.status = DeliveryStatus.SEEN;
	}

}