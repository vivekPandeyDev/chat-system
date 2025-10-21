package com.loop.troop.chat.infrastructure.jpa.repository;

import com.loop.troop.chat.domain.enums.RoomType;

import java.util.UUID;

/**
 * Projection for {@link com.loop.troop.chat.infrastructure.jpa.entity.ChatRoomEntity}
 */
public interface ChatRoomEntityInfo {
    UUID getRoomId();

    RoomType getType();

    String getGroupName();

    boolean isIsPermanent();

    String getImagePath();
}