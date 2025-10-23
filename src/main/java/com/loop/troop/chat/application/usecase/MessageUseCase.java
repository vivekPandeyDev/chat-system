package com.loop.troop.chat.application.usecase;

import com.loop.troop.chat.application.command.CreateMessageCommand;
import com.loop.troop.chat.application.dto.PageResponse;
import com.loop.troop.chat.application.dto.PaginationQuery;
import com.loop.troop.chat.domain.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface MessageUseCase {
    void deleteMessge(@NotBlank String roomId,@NotBlank String messageId);
	Message createMessage(@Valid CreateMessageCommand command);

	void sendMessage(@NotNull Message message);

	PageResponse<Message> fetchMessageByRoomId(@NotBlank String roomId, @NotNull PaginationQuery query);

}
