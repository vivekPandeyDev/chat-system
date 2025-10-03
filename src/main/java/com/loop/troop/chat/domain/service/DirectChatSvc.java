package com.loop.troop.chat.domain.service;

import com.loop.troop.chat.domain.message.Message;

import java.util.ArrayList;
import java.util.List;

public final class DirectChatSvc implements ChatService {
    private final List<Message> store = new ArrayList<>();

    @Override
    public void sendMessage(Message msg) { store.add(msg); }

    @Override
    public List<Message> fetchMessages(String roomId) {
        return store.stream().filter(m -> m.getRoom().getRoomId().equals(roomId)).toList();
    }
}