package com.loop.troop.chat.infrastructure.shared.mapper;

import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.domain.Notification;
import com.loop.troop.chat.domain.User;
import com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity;
import com.loop.troop.chat.infrastructure.jpa.entity.MessageEntity;
import com.loop.troop.chat.infrastructure.jpa.entity.NotificationEntity;

public class NotificationMapper {

	private NotificationMapper() {
	}

	public static NotificationEntity toEntity(Notification domain) {
		return NotificationEntity.builder()
			.notificationId(domain.getNotificationId())
			.user(UserMapper.toEntity(domain.getUser()))
			.room(ChatRoomEntity.builder().roomId(domain.getRoom().getRoomId()).build()) // reference
																							// only
			.message(MessageEntity.builder().messageId(domain.getMessage().getMessageId()).build()) // reference
																									// only
			.type(domain.getType())
			.isRead(domain.isRead())
			.createdAt(domain.getCreatedAt())
			.build();
	}

	public static Notification toDomain(NotificationEntity entity, User user, ChatRoom room, Message message) {
		return new Notification(entity.getNotificationId(), user, room, message, entity.getType());
	}

}
