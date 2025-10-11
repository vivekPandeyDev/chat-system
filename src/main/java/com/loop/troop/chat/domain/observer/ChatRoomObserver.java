package com.loop.troop.chat.domain.observer;

import com.loop.troop.chat.domain.event.ChatEvent;

public interface ChatRoomObserver {

	void update(ChatEvent event);

}
