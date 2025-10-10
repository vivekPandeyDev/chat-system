package com.loop.troop.chat.application.usecase.room;

import jakarta.validation.constraints.NotBlank;

public interface GroupChatRoomUseCase extends ChatRoomUseCase{
    void addParticipant(@NotBlank String roomId,@NotBlank String userId);
    void removeParticipant(@NotBlank String roomId,@NotBlank String userId);
}
