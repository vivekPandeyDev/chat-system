package com.loop.troop.chat.domain.chat;

import com.loop.troop.chat.domain.enums.EventType;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.message.Message;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
import com.loop.troop.chat.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract sealed class ChatRoom permits SingleChatRoom, GroupChatRoom {
    protected final String roomId;
    protected final RoomType type;
    protected final User createdBy;
    protected final LocalDateTime createdAt;
    protected boolean isActive;
    protected final List<User> participants = new ArrayList<>();
    protected final List<ChatRoomObserver> observers = new ArrayList<>();

    protected ChatRoom(String roomId, RoomType type, User createdBy) {
        this.roomId = roomId;
        this.type = type;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void addParticipant(User user) { participants.add(user); }
    public void removeParticipant(User user) { participants.remove(user); }
    public List<User> getParticipants() { return List.copyOf(participants); }

    public void addObserver(ChatRoomObserver obs) { observers.add(obs); }
    public void removeObserver(ChatRoomObserver obs) { observers.remove(obs); }

    public void notifyObservers(ChatEvent event) {
        observers.forEach(o -> o.update(event));
    }

    public void sendMessage(Message msg) {
        var event = new ChatEvent(EventType.MESSAGE_SENT, roomId, msg, LocalDateTime.now());
        notifyObservers(event);
    }
}