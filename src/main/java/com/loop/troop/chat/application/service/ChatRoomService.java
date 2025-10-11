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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService implements ChatRoomUseCase {

	private final UserPersistence userPersistence;

	private final ChatRoomPersistence chatRoomPersistence;

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
		var savedRoom = chatRoomPersistence.save(room);
		return savedRoom.getRoomId();
	}

	@Override
	public PageResponse<ChatRoom> fetchChatRoom(PaginationQuery paginationQuery) {
		return null;
	}

	@Override
	public Optional<ChatRoom> getChatRoomById(String roomId) {
		return Optional.empty();
	}

}
