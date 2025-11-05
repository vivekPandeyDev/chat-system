package com.loop.troop.chat.application.observer;

import com.loop.troop.chat.domain.event.ChatRoomEvent;
import com.loop.troop.chat.domain.event.enums.ChatRoomEventType;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class DeliveryTracker implements ChatRoomObserver {


    @Override
    public void onEvent(ChatRoomEvent event) {
        if (Objects.requireNonNull(event.type()) == ChatRoomEventType.MESSAGE_SENT) {
            log.info("message sent event");
            log.info("delivery tracker chat room : {}", event.type());
        } else {
            log.info("default action performed");
            log.info("Please check the event type");
        }
        log.info("DeliveryTracker updated status: {}", event.eventId());
    }
}