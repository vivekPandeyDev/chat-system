package com.loop.troop.chat.application.service.room;

import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.ChatRoomPersistence;
import com.loop.troop.chat.application.usecase.ChatRoomUseCase;
import com.loop.troop.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomService implements ChatRoomUseCase {

	private final ChatRoomPersistence chatRoomPersistence;

	@Override
	public PageResponse<ChatRoom> fetchChatRoom(PaginationQuery query) {
		log.info("ChatRoomService::fetchChatRoom; page-offset: {}, page-size: {}, page-by: {}, page-dir: {}",
				query.page(), query.size(), query.sortBy(), query.sortDir());
		return chatRoomPersistence.findAll(query);
	}

	@Override
	public Optional<ChatRoom> getChatRoomById(String roomId) {
		log.info("room-id to fetch user: {}", roomId);
		return chatRoomPersistence.findById(roomId);
	}

}
