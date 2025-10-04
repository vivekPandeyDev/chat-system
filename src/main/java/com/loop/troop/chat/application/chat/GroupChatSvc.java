package com.loop.troop.chat.application.chat;


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
public class GroupChatSvc implements ChatService {

    private final MessageRepository messageRepository;

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
        return entities.stream()
                .map(MessageMapper::toDomain)
                .toList();
    }
}
