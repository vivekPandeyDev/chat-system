package com.loop.troop.chat.application.observer;

import com.loop.troop.chat.domain.event.ChatEvent;
import com.loop.troop.chat.domain.service.ChatRoomObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DeliveryTracker implements ChatRoomObserver {

	@Override
	public void update(ChatEvent event) {
		switch (event.type()) {
			case MESSAGE_SENT -> event.message().markDelivered();
			case MESSAGE_DELIVERED -> event.message().markSeen();
			default -> {
				log.info("default action performed");
				log.info("Please check the event type");
			}
		}
		log.info("DeliveryTracker updated status: {}", event.message());
	}

}