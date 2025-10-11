package com.loop.troop.chat.application.service;

import com.loop.troop.chat.application.command.CreateChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.ChatRoomPersistence;
import com.loop.troop.chat.application.persistence.UserPersistence;
import com.loop.troop.chat.application.usecase.room.ChatRoomUseCase;
import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.GroupChatRoom;
import com.loop.troop.chat.domain.SingleChatRoom;
import com.loop.troop.chat.domain.enums.RoomType;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.service.ChatRoomObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService implements ChatRoomUseCase {

	private final UserPersistence userPersistence;

	private final ChatRoomPersistence chatRoomPersistence;
    private final List<ChatRoomObserver> observerList;

	@Override
	public String createRoom(CreateChatRoomCommand request) {
		var owner = userPersistence.findById(request.getCreatedById())
			.orElseThrow(() -> UserServiceException.userNotFound(request.getCreatedById()));
		ChatRoom room;
		if (request.getRoomType().equals(RoomType.SINGLE)) {
			var otherParticipant = userPersistence.findById(request.getOtherParticipantId())
				.orElseThrow(() -> UserServiceException.userNotFound(request.getCreatedById()));
			room = new SingleChatRoom(null, owner, otherParticipant);
		}
		else {
			var participants = userPersistence.fetchUsersById(request.getInitialParticipantIds());
			room = new GroupChatRoom(null, owner, request.getGroupName(), Boolean.TRUE.equals(request.getIsPermanent()),
					participants);
		}
        observerList.forEach(room::addObserver);
		var savedRoom = chatRoomPersistence.save(room);
		return savedRoom.getRoomId();
	}

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
