package com.loop.troop.chat.application.usecase;

import com.loop.troop.chat.application.command.CreateGroupChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.domain.ChatRoom;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface ChatRoomUseCase {

	PageResponse<ChatRoom> fetchChatRoom(@Valid PaginationQuery paginationQuery);

	Optional<ChatRoom> getChatRoomById(@NotNull String roomId);

}
