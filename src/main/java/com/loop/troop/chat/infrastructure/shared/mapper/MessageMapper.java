package com.loop.troop.chat.infrastructure.shared.mapper;

import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.infrastructure.jpa.entity.MessageEntity;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageResponseDto;

import java.util.Objects;
import java.util.UUID;

public class MessageMapper {

	private MessageMapper() {
	}

	public static Message toDomain(MessageEntity entity) {
		if (entity == null)
			return null;

		var room = ChatRoomMapper.toDomain(entity.getRoom());
		var sender = UserMapper.toDomain(entity.getSender());

		var msg = new Message(toString(entity.getMessageId()), room, sender, entity.getContent(), entity.getType());
		msg.setStatus(entity.getStatus());
		return msg;
	}

	public static MessageEntity toEntity(Message domain) {
		if (domain == null)
			return null;

		return MessageEntity.builder()
			.messageId(toUuid(domain.getMessageId()))
			.room(ChatRoomMapper.toEntity(domain.getRoom()))
			.sender(UserMapper.toEntity(domain.getSender()))
			.content(domain.getContent())
			.type(domain.getType())
			.sentAt(domain.getSentAt())
			.status(domain.getStatus())
			.build();
	}

	public static MessageResponseDto toResponseDto(Message msg) {
		return MessageResponseDto.builder()
			.messageId(msg.getMessageId())
			.roomId(msg.getRoom().getRoomId())
			.senderId(msg.getSender().getUserId())
			.content(msg.getContent())
			.type(msg.getType())
			.sentAt(msg.getSentAt())
			.status(msg.getStatus())
			.build();
	}

	public static String toString(UUID uuid) {
		if (Objects.nonNull(uuid)) {
			return uuid.toString();
		}
		else {
			return null;
		}
	}

	public static UUID toUuid(String id) {
		if (Objects.nonNull(id)) {
			return UUID.fromString(id);
		}
		else {
			return null;
		}
	}

}
