package com.loop.troop.chat.application.projection;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRoomProjection {
    private String groupId;
    private String roomType;
    private String roomName;
    private String imagePath;
    private String imageUrl;

    public ChatRoomProjection(String groupId, String roomType, String roomName, String imagePath) {
        this.groupId = groupId;
        this.roomType = roomType;
        this.roomName = roomName;
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ChatRoomProjection{" +
                "groupId='" + groupId + '\'' +
                ", roomType='" + roomType + '\'' +
                ", groupName='" + roomName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
