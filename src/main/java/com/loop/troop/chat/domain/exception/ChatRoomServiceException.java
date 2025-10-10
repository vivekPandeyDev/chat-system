package com.loop.troop.chat.domain.exception;

import org.springframework.http.HttpStatus;

public class ChatRoomServiceException extends ServiceException{
    protected ChatRoomServiceException(String errorCode, String userMessage, HttpStatus status) {
        super(errorCode, userMessage, status);
    }
    public static ChatRoomServiceException roomNotFound(String roomId) {
        return new ChatRoomServiceException(
                "CHAT_ROOM_NOT_FOUND",
                "Chat room with ID '" + roomId + "' not found",
                HttpStatus.NOT_FOUND
        );
    }
}
