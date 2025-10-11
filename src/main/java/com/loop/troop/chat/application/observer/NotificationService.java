package com.loop.troop.chat.application.observer;

import com.loop.troop.chat.domain.event.ChatEvent;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NotificationService implements ChatRoomObserver {

	@Override
	public void update(ChatEvent event) {
		log.info("NotificationService: Sending notification for event {}", event.type());
	}

}