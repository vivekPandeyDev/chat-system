package com.loop.troop.chat.domain;


import com.loop.troop.chat.domain.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Notification {
    private final String notificationId;
    private final User user;
    private final ChatRoom room;
    private final Message message;
    private final NotificationType type;
    private boolean isRead;
    private final LocalDateTime createdAt;

    public Notification(String notificationId, User user, ChatRoom room, Message message, NotificationType type) {
        this.notificationId = notificationId;
        this.user = user;
        this.room = room;
        this.message = message;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() { this.isRead = true; }
}