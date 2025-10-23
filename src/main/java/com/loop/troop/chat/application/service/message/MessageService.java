package com.loop.troop.chat.application.service.message;

import com.loop.troop.chat.application.command.CreateMessageCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.ChatRoomPersistence;
import com.loop.troop.chat.application.persistence.MessagePersistence;
import com.loop.troop.chat.application.persistence.UserPersistence;
import com.loop.troop.chat.application.usecase.MessageUseCase;
import com.loop.troop.chat.domain.Message;
import com.loop.troop.chat.domain.enums.MessageType;
import com.loop.troop.chat.domain.exception.ChatRoomServiceException;
import com.loop.troop.chat.domain.exception.UserServiceException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@Validated
public class MessageService implements MessageUseCase {

	private final MessagePersistence messagePersistence;

	private final ChatRoomPersistence chatRoomPersistence;

	private final UserPersistence userPersistence;

    @Override
    public void deleteMessge(@NotBlank String roomId,@NotBlank String messageId) {

    }

    @Override
	public Message createMessage(@Valid CreateMessageCommand command) {
		var chatRoom = chatRoomPersistence.findById(command.roomId())
			.orElseThrow(() -> ChatRoomServiceException.roomNotFound(command.roomId()));
		var user = userPersistence.findById(command.userId())
			.orElseThrow(() -> UserServiceException.userNotFound(command.userId()));
		var message = new Message(chatRoom, user, command.content(), MessageType.valueOf(command.messageType()));
		return messagePersistence.save(message);

	}

	@Override
	public void sendMessage(Message message) {
		log.info("........................................sending message.......................");
	}

	@Override
	public PageResponse<Message> fetchMessageByRoomId(@NotBlank String roomId, @NotNull PaginationQuery query) {
		return messagePersistence.findAllByRoomId(roomId, query);
	}

}
