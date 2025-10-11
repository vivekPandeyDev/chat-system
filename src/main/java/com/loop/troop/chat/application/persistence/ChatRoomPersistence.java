package com.loop.troop.chat.application.persistence;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.domain.ChatRoom;
import java.util.Optional;

public interface ChatRoomPersistence {

	ChatRoom save(ChatRoom chatRoom);

	Optional<ChatRoom> findById(String chatRoomId);

	PageResponse<ChatRoom> findAll(PaginationQuery paginationQuery);

}
