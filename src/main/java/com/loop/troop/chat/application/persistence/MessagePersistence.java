package com.loop.troop.chat.application.persistence;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.domain.Message;

import java.util.Optional;

public interface MessagePersistence {

	Message save(Message message);

	Optional<Message> findById(String messageId);

	PageResponse<Message> findAllByRoomId(String roomId, PaginationQuery paginationQuery);

}
