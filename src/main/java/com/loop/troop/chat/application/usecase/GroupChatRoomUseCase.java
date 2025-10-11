package com.loop.troop.chat.application.usecase;

import jakarta.validation.constraints.NotBlank;

public interface GroupChatRoomUseCase {

	void addParticipants(@NotBlank String roomId, @NotBlank String userId);

	void removeParticipants(@NotBlank String roomId, @NotBlank String userId);

}
