package com.loop.troop.chat.infrastructure.shared.mapper;

import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.domain.service.ChatRoomObserver;
import com.loop.troop.chat.infrastructure.jpa.entity.MessageEntity;
import com.loop.troop.chat.infrastructure.shared.dto.message.MessageResponseDto;

import java.util.List;

public class MessageMapper {


    private MessageMapper() {
    }

    public static Message toDomain(MessageEntity entity) {
        if (entity == null) return null;

        var room = ChatRoomMapper.toDomain(entity.getRoom());
        var sender = UserMapper.toDomain(entity.getSender());

        var msg = new Message(
                entity.getMessageId(),
                room,
                sender,
                entity.getContent(),
                entity.getType()
        );
        msg.setStatus(entity.getStatus());
        return msg;
    }

    public static MessageEntity toEntity(Message domain) {
        if (domain == null) return null;

        return MessageEntity.builder()
                .messageId(domain.getMessageId())
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
}
