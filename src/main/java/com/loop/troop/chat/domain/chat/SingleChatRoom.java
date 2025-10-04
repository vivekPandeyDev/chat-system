package com.loop.troop.chat.domain.chat;

import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.user.User;
import org.jetbrains.annotations.NotNull;

public final class SingleChatRoom extends ChatRoom {
    public SingleChatRoom(String roomId, @NotNull User createdBy, @NotNull User other) {
        super(roomId, RoomType.SINGLE, createdBy);
        addParticipant(createdBy);
        addParticipant(other);
    }
}