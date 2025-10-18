package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.RoomType;
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

    public GroupChatRoom(String roomId, User createdBy, String groupName, boolean isPermanent,
                         List<User> initialParticipants) {
        super(roomId, RoomType.GROUP, createdBy);
        this.groupName = groupName;
        this.isPermanent = isPermanent;
        this.admins.add(createdBy);
        initialParticipants.forEach(this::addParticipant);
    }

    public GroupChatRoom(String roomId, User createdBy, String groupName, boolean isPermanent,
                         List<User> initialParticipants, List<User> admins) {
        super(roomId, RoomType.GROUP, createdBy);
        this.groupName = groupName;
        this.isPermanent = isPermanent;
        this.admins.addAll(admins);
        initialParticipants.forEach(this::addParticipant);
    }

}