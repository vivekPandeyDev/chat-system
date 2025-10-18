package com.loop.troop.chat.application.service.room;

import com.loop.troop.chat.application.command.CreateGroupChatRoomCommand;
import com.loop.troop.chat.application.persistence.ChatRoomPersistence;
import com.loop.troop.chat.application.persistence.UserPersistence;
import com.loop.troop.chat.application.usecase.GroupChatRoomUseCase;
import com.loop.troop.chat.domain.GroupChatRoom;
import com.loop.troop.chat.domain.exception.UserServiceException;
import com.loop.troop.chat.domain.observer.ChatRoomObserver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupChatRoomService implements GroupChatRoomUseCase {

	private final UserPersistence userPersistence;

	private final ChatRoomPersistence chatRoomPersistence;

	private final List<ChatRoomObserver> observerList;

	@Override
	public String createGroupChatRoom(@Valid CreateGroupChatRoomCommand command) {
		var owner = userPersistence.findById(command.createdById())
			.orElseThrow(() -> UserServiceException.userNotFound(command.createdById()));
		var participants = userPersistence.fetchUsersById(command.participantIds());
		var groupChatRoom = new GroupChatRoom(null, owner, command.groupName(), command.isPermanent(), participants);
		observerList.forEach(groupChatRoom::addObserver);
		var savedRoom = chatRoomPersistence.save(groupChatRoom);
		return savedRoom.getRoomId();
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
