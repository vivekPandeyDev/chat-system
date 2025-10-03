package com.loop.troop.chat.domain.user;

import com.loop.troop.chat.domain.chat.ChatRoom;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.message.Message;
import com.loop.troop.chat.domain.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class User {
    private final String userId;
    private String username;
    private String email;
    private String avatarUrl;
    private UserStatus status;

    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.status = UserStatus.OFFLINE;
    }


    public void sendMessage(ChatRoom room, Message msg) {
        room.sendMessage(msg);
    }

    public void receiveNotification(Notification n) {
        log.info("User {} received notification: {}", username, n);
    }

}