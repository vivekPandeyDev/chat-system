package com.loop.troop.chat.infrastructure.shared.mapper;

import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.domain.Notification;
import com.loop.troop.chat.domain.User;
import com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity;
import com.loop.troop.chat.infrastructure.jpa.entity.MessageEntity;
import com.loop.troop.chat.infrastructure.jpa.entity.NotificationEntity;

import java.util.Objects;
import java.util.UUID;

public class NotificationMapper {

	private NotificationMapper() {
	}

	public static NotificationEntity toEntity(Notification domain) {
		return NotificationEntity.builder()
			.notificationId(toUuid(domain.getNotificationId()))
			.user(UserMapper.toEntity(domain.getUser()))
			.room(ChatRoomEntity.builder().roomId(UUID.fromString(domain.getRoom().getRoomId())).build()) // reference
			// only
			.message(MessageEntity.builder().messageId(toUuid(domain.getMessage().getMessageId())).build()) // reference
			// only
			.type(domain.getType())
			.isRead(domain.isRead())
			.createdAt(domain.getCreatedAt())
			.build();
	}

	public static Notification toDomain(NotificationEntity entity, User user, ChatRoom room, Message message) {
		return new Notification(toString(entity.getNotificationId()), user, room, message, entity.getType());
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
