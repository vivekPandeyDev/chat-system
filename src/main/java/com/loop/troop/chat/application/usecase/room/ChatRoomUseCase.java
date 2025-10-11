package com.loop.troop.chat.application.usecase.room;

import com.loop.troop.chat.application.command.CreateChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.domain.ChatRoom;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface ChatRoomUseCase {

	String createRoom(@Valid CreateChatRoomCommand request);

	PageResponse<ChatRoom> fetchChatRoom(@Valid PaginationQuery paginationQuery);

	Optional<ChatRoom> getChatRoomById(@NotNull String roomId);

}
