package com.loop.troop.chat.domain.observer;

import com.loop.troop.chat.domain.chat.ChatEvent;

public sealed interface ChatRoomObserver permits NotificationService, DeliveryTracker{
    void update(ChatEvent event);
}
