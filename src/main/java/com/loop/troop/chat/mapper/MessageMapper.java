package com.loop.troop.chat.mapper;

import com.loop.troop.chat.domain.chat.ChatRoom;
import com.loop.troop.chat.domain.message.Message;
import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.persistance.entity.ChatRoomEntity;
import com.loop.troop.chat.persistance.entity.MessageEntity;

public class MessageMapper {

    private MessageMapper() {
    }

    public static MessageEntity toEntity(Message domain) {
        return MessageEntity.builder()
                .messageId(domain.getMessageId())
                .room(ChatRoomEntity.builder().roomId(domain.getRoom().getRoomId()).build()) // reference only
                .sender(UserMapper.toEntity(domain.getSender()))
                .content(domain.getContent())
                .type(domain.getType())
                .sentAt(domain.getSentAt())
                .status(domain.getStatus())
                .build();
    }

    public static Message toDomain(MessageEntity entity, ChatRoom room, User sender) {
        return new Message(entity.getMessageId(), room, sender, entity.getContent(), entity.getType());
    }
}