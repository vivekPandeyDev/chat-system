package com.loop.troop.chat.infrastructure.shared.mapper;


import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.GroupChatRoom;
import com.loop.troop.chat.domain.SingleChatRoom;
import com.loop.troop.chat.domain.service.ChatRoomObserver;
import com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity;
import com.loop.troop.chat.domain.enums.RoomType;

import java.util.List;


public class ChatRoomMapper {

    private ChatRoomMapper() {
    }

    public static ChatRoom toDomain(ChatRoomEntity entity) {
        if (entity == null) return null;

        var creator = UserMapper.toDomain(entity.getCreatedBy());
        var participants = entity.getParticipants().stream()
                .map(UserMapper::toDomain)
                .toList();
        ChatRoom room;
        if (entity.getType() == RoomType.SINGLE) {
            if (participants.isEmpty()){
                throw new IllegalArgumentException("Must have other participant for single chat room");
            }
            room = new SingleChatRoom(entity.getRoomId(), creator, participants.get(1) );
        } else { // GROUP
            var group = new GroupChatRoom(entity.getRoomId(), creator, entity.getGroupName(), entity.isPermanent(),participants);
            group.setActive(entity.isActive());
            // map participants
            entity.getParticipants().forEach(u -> group.addParticipant(UserMapper.toDomain(u)));
            // map admins
            entity.getAdmins().forEach(u -> group.getAdmins().add(UserMapper.toDomain(u)));
            room = group;
        }

        room.setActive(entity.isActive());
        return room;
    }

    public static ChatRoomEntity toEntity(ChatRoom domain) {
        if (domain == null) return null;

        var builder = ChatRoomEntity.builder()
                .roomId(domain.getRoomId())
                .type(domain.getType())
                .createdBy(UserMapper.toEntity(domain.getCreatedBy()))
                .createdAt(domain.getCreatedAt())
                .isActive(domain.isActive());

        // map participants
        builder.participants(domain.getParticipants().stream()
                .map(UserMapper::toEntity)
                .toList());

        if (domain instanceof GroupChatRoom g) {
            builder.groupName(g.getGroupName());
            builder.isPermanent(g.isPermanent());
            builder.admins(g.getAdmins().stream()
                    .map(UserMapper::toEntity)
                    .toList());
        }

        return builder.build();
    }
}
