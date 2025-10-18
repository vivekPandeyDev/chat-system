package com.loop.troop.chat.application.service.room;

import com.loop.troop.chat.application.command.CreateGroupChatRoomCommand;
import com.loop.troop.chat.application.command.CreateSingleChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.persistence.ChatRoomPersistence;
import com.loop.troop.chat.application.persistence.UserPersistence;
import com.loop.troop.chat.application.usecase.ChatRoomUseCase;
import com.loop.troop.chat.domain.ChatRoom;
import com.loop.troop.chat.domain.SingleChatRoom;
import com.loop.troop.chat.domain.exception.UserServiceException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@Validated
public class ChatRoomService implements ChatRoomUseCase {

	private final ChatRoomPersistence chatRoomPersistence;
    private final UserPersistence userPersistence;
    @Override
    public String createSingleChatRoom(CreateSingleChatRoomCommand command) {
        var owner = userPersistence.findById(command.createdById())
                .orElseThrow(() -> UserServiceException.userNotFound(command.createdById()));

        var otherParticipant = userPersistence.findById(command.otherParticipantsId())
                .orElseThrow(() -> UserServiceException.userNotFound(command.otherParticipantsId()));
        var chatRoom = new SingleChatRoom(null,owner,otherParticipant);
        return chatRoomPersistence.save(chatRoom).getRoomId();
    }

    @Override
	public PageResponse<ChatRoom> fetchChatRoomPerUser(@Valid PaginationQuery query, @NotNull String userId) {
		log.info("ChatRoomService::fetchChatRoom; page-offset: {}, page-size: {}, page-by: {}, page-dir: {}",
				query.page(), query.size(), query.sortBy(), query.sortDir());
		return chatRoomPersistence.findChatRoomByUserId(query,userId);
	}

	@Override
	public Optional<ChatRoom> getChatRoomById(@NotNull String roomId) {
		log.info("room-id to fetch user: {}", roomId);
		return chatRoomPersistence.findById(roomId);
	}

}
