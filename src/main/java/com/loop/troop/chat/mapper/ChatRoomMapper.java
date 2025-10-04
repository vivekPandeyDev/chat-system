package com.loop.troop.chat.mapper;


import com.loop.troop.chat.domain.chat.ChatRoom;
import com.loop.troop.chat.domain.chat.GroupChatRoom;
import com.loop.troop.chat.domain.chat.SingleChatRoom;
import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.adapter.persistance.entity.ChatRoomEntity;


public class ChatRoomMapper {

    private ChatRoomMapper() {
    }

    public static ChatRoomEntity toEntity(ChatRoom domain) {
        ChatRoomEntity.ChatRoomEntityBuilder builder = ChatRoomEntity.builder()
                .roomId(domain.getRoomId())
                .type(domain.getType())
                .createdBy(UserMapper.toEntity(domain.getCreatedBy()))
                .createdAt(domain.getCreatedAt())
                .isActive(domain.isActive());

        if (domain instanceof GroupChatRoom g) {
            builder.groupName(g.getGroupName());
            builder.isPermanent(g.isPermanent());
        }

        return builder.build();
    }

    public static ChatRoom toDomain(ChatRoomEntity entity, User createdBy) {
        return switch (entity.getType()) {
            case SINGLE -> new SingleChatRoom(
                    entity.getRoomId(),
                    createdBy,
                    null // participants loaded separately
            );
            case GROUP -> new GroupChatRoom(
                    entity.getRoomId(),
                    createdBy,
                    entity.getGroupName(),
                    entity.isPermanent()
            );
        };
    }
}
