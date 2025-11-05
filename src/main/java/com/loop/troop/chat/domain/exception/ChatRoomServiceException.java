package com.loop.troop.chat.domain.exception;

import org.springframework.http.HttpStatus;

import static com.loop.troop.chat.domain.constant.DomainConstant.CHAT_ROOM_FOUND;
import static com.loop.troop.chat.domain.constant.DomainConstant.CHAT_ROOM_NOT_FOUND;

public class ChatRoomServiceException extends ServiceException {

	protected ChatRoomServiceException(String errorCode, String userMessage, HttpStatus status) {
		super(errorCode, userMessage, status);
	}

	public static ChatRoomServiceException roomNotFound(String roomId) {
		return new ChatRoomServiceException(CHAT_ROOM_NOT_FOUND.getValue(),
				"Chat room with ID '" + roomId + "' not found", HttpStatus.NOT_FOUND);
	}

	public static ChatRoomServiceException roomFoundByName(String roomName) {
		return new ChatRoomServiceException(CHAT_ROOM_FOUND.getValue(), "Chat room with name '" + roomName + "' found",
				HttpStatus.BAD_REQUEST);
	}

}
