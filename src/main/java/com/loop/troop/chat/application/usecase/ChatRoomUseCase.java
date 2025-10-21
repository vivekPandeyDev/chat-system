package com.loop.troop.chat.application.usecase;

import com.loop.troop.chat.application.command.CreateSingleChatRoomCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.application.projection.ChatRoomProjection;
import com.loop.troop.chat.domain.ChatRoom;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface ChatRoomUseCase {
    String createSingleChatRoom(@Valid CreateSingleChatRoomCommand request);

    PageResponse<ChatRoom> fetchChatRoomPerUser(@Valid PaginationQuery paginationQuery, @NotNull String userId);

    PageResponse<ChatRoomProjection> fetchChatRoomProjectionPerUser(@Valid PaginationQuery paginationQuery, @NotNull String userId);

    Optional<ChatRoom> getChatRoomById(@NotNull String roomId);

}
