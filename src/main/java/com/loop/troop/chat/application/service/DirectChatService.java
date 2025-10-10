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
public class DirectChatService implements ChatService {

    private final MessageRepository messageRepository;
    private final List<ChatRoomObserver> chatRoomObservers;

    @Override
    public void sendMessage(Message msg) {
        // convert domain -> entity
        MessageEntity entity = MessageMapper.toEntity(msg);

        // save message
        messageRepository.save(entity);

    }

    @Override
    public List<Message> fetchMessages(String roomId) {
        // fetch entity list
        List<MessageEntity> entities = messageRepository.findByRoomId(roomId);

        // map to domain
        return entities.stream()
                .map(MessageMapper::toDomain)
                .toList();
    }
}
