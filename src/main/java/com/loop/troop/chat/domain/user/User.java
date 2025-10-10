package com.loop.troop.chat.domain.user;

import com.loop.troop.chat.domain.chat.ChatRoom;
import com.loop.troop.chat.domain.enums.UserStatus;
import com.loop.troop.chat.domain.message.Message;
import com.loop.troop.chat.domain.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Getter
@Setter
@Slf4j
@ToString
public class User {
    private String userId;
    private String username;
    private String email;
    private String avatarUrl;
    private UserStatus status;

    public User(String username, String email,String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.email = email;
        this.status = UserStatus.OFFLINE;
    }

    public void sendMessage(ChatRoom room, Message msg) {
        room.sendMessage(msg);
    }

    public void updateStatus(UserStatus status){
        if (Objects.isNull(status)){
            throw new IllegalStateException("User status is required");
        }
        this.status=status;
    }

    public void receiveNotification(Notification n) {
        log.info("User {} received notification: {}", username, n);
    }

}