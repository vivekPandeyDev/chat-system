package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.DeliveryStatus;
import com.loop.troop.chat.domain.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Message {

	private String messageId;

	private ChatRoom room;

	private User sender;

	private String content;

	private MessageType type;

	private LocalDateTime sentAt;

	private DeliveryStatus status;

	public Message(String messageId, ChatRoom room, User sender, String content, MessageType type) {
		this(room, sender, content, type);
		this.messageId = messageId;
	}

	public Message(ChatRoom room, User sender, String content, MessageType type) {
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