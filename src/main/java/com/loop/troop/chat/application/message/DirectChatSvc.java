package com.loop.troop.chat.application.message;


import com.loop.troop.chat.application.persistance.entity.MessageEntity;
import com.loop.troop.chat.application.persistance.repo.MessageRepository;
import com.loop.troop.chat.domain.message.Message;
import com.loop.troop.chat.domain.service.ChatService;
import com.loop.troop.chat.shared.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectChatSvc implements ChatService {

    private final MessageRepository messageRepository;

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
