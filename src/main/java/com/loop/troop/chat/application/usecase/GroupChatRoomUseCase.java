package com.loop.troop.chat.application.usecase;

import com.loop.troop.chat.application.command.CreateGroupChatRoomCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface GroupChatRoomUseCase {

	String createGroupChatRoom(@Valid CreateGroupChatRoomCommand request);

	void addParticipants(@NotBlank String roomId, @NotBlank String userId);

	void removeParticipants(@NotBlank String roomId, @NotBlank String userId);

}
