package com.loop.troop.chat.domain;

import com.loop.troop.chat.domain.enums.MessageType;
import com.loop.troop.chat.domain.event.room.ChatRoomCreatedEvent;
import com.loop.troop.chat.domain.event.room.ChatRoomMessageSendEvent;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChatRoomTest {

	private User creator;

	private User otherUser;

	private ChatRoomObserver observer;

	@BeforeEach
	void setUp() {
		creator = new User("user1", "Alice");
		creator.setUserId("user-1");
		otherUser = new User("user2", "Bob");
		otherUser.setUserId("user-2");
		observer = Mockito.mock(ChatRoomObserver.class);
	}

	@Test
	void shouldRecordEventOnChatRoomCreation() {
		// given
		var chatRoom = new SingleChatRoom("room1", creator, otherUser);
		chatRoom.addObserver(observer);

		// when
		var events = chatRoom.releaseEvents();

		// then
		assertThat(events).hasSize(1);
		assertThat(events.getFirst()).isInstanceOf(ChatRoomCreatedEvent.class);

		var createdEvent = (ChatRoomCreatedEvent) events.getFirst();
		assertThat(createdEvent.aggregateId()).isEqualTo("room1");
		assertThat(createdEvent.roomName()).isEqualTo("user2");
		assertThat(createdEvent.createdBy().getUserId()).isEqualTo(creator.getUserId());

		verify(observer, times(1)).onEvent(any(ChatRoomCreatedEvent.class));
	}

	@Test
	void shouldRecordAndNotifyMessageEvent() {
		// given
		var chatRoom = new SingleChatRoom("room1", creator, otherUser);
		chatRoom.addObserver(observer);
		chatRoom.releaseEvents(); // clear creation event before sending message

		var message = new Message(chatRoom, creator, "Hello Bob!", MessageType.TEXT);

		// when
		chatRoom.sendMessage(message);
		var events = chatRoom.releaseEvents();

		// then
		assertThat(events).hasSize(1);
		assertThat(events.getFirst()).isInstanceOf(ChatRoomMessageSendEvent.class);

		var event = (ChatRoomMessageSendEvent) events.getFirst();
		assertThat(event.message().getContent()).isEqualTo("Hello Bob!");
		assertThat(event.aggregateId()).isEqualTo(message.getMessageId());
		assertThat(event.message().getSender()).isEqualTo(creator);
		assertThat(event.message().getRoom()).isEqualTo(chatRoom);

		// observer should receive the event
		verify(observer, times(1)).onEvent(any(ChatRoomMessageSendEvent.class));
	}

}
