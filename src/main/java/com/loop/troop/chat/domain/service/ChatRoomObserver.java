package com.loop.troop.chat.domain.service;

import com.loop.troop.chat.domain.event.ChatEvent;

public interface ChatRoomObserver {
    void update(ChatEvent event);
}
