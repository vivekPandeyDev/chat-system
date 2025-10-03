package com.loop.troop.chat.domain.chat;

import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.user.User;

public final class SingleChatRoom extends ChatRoom {
    public SingleChatRoom(String roomId, User createdBy, User other) {
        super(roomId, RoomType.SINGLE, createdBy);
        addParticipant(createdBy);
        addParticipant(other);
    }
}