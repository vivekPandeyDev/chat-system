package com.loop.troop.chat.domain.chat;

import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class GroupChatRoom extends ChatRoom {
    private boolean isPermanent;
    private String groupName;
    private final List<User> admins = new ArrayList<>();

    public GroupChatRoom(String roomId, User createdBy, String groupName, boolean isPermanent,List<User> initialParticipants) {
        super(roomId, RoomType.GROUP, createdBy);
        this.groupName = groupName;
        this.isPermanent = isPermanent;
        admins.add(createdBy);
        initialParticipants.forEach(this::addParticipant);
    }
}