package com.loop.troop.chat.application.service;

import com.loop.troop.chat.domain.service.ChatRoomObserver;
import com.loop.troop.chat.infrastructure.jpa.entity.MessageEntity;
import com.loop.troop.chat.infrastructure.jpa.repository.MessageRepository;
import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.domain.service.ChatService;
import com.loop.troop.chat.infrastructure.shared.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatService implements ChatService {

	private final MessageRepository messageRepository;

	private final List<ChatRoomObserver> chatRoomObservers;

	@Override
	public void sendMessage(Message msg) {
		MessageEntity entity = MessageMapper.toEntity(msg);
		messageRepository.save(entity);

		// Notify observers (domain layer)
		msg.getRoom().sendMessage(msg);

		// TODO: Publish event to RabbitMQ/WebSocket for real-time notifications
	}

	@Override
	public List<Message> fetchMessages(String roomId) {
		List<MessageEntity> entities = messageRepository.findByRoomId(roomId);
		return entities.stream().map(entity -> MessageMapper.toDomain(entity, chatRoomObservers)).toList();
	}

}
