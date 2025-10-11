package com.loop.troop.chat.application.service.room;

import com.loop.troop.chat.application.persistence.ChatRoomPersistence;
import com.loop.troop.chat.application.usecase.GroupChatRoomUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupChatRoomService implements GroupChatRoomUseCase {

	private final ChatRoomPersistence chatRoomPersistence;

	@Override
	public void addParticipants(String roomId, String userId) {
		log.info("adding participant for : {}", roomId);
		chatRoomPersistence.addParticipants(roomId, userId);
	}

	@Override
	public void removeParticipants(String roomId, String userId) {
		log.info("removing participant for : {}", roomId);
		chatRoomPersistence.removeParticipants(roomId, userId);
	}

}
