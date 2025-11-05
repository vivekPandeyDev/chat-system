package com.loop.troop.chat.domain.observer;

import com.loop.troop.chat.domain.event.ChatRoomEvent;

public interface ChatRoomObserver extends DomainObserver<ChatRoomEvent> {
    @Override
    void onEvent(ChatRoomEvent event);
}
