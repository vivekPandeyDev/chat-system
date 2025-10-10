package com.loop.troop.chat.application.usecase.room;

public interface TempChatRoomUseCase extends ChatRoomUseCase {
    void expireRoom(String roomId);
}