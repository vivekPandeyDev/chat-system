package com.loop.troop.chat.domain.exception;

import org.springframework.http.HttpStatus;

public class UserServiceException extends ServiceException {

	public UserServiceException(String errorCode, String userMessage, HttpStatus status) {
		super(errorCode, userMessage, status);
	}

	public static UserServiceException userNotFound(String userId) {
		return new UserServiceException("USER_NOT_FOUND", "User with ID '" + userId + "' not found",
				HttpStatus.NOT_FOUND);
	}
    public static UserServiceException userNotFoundWithEmail(String email) {
        return new UserServiceException("USER_EMAIL_NOT_FOUND", "User with email '" + email + "' not found",
                HttpStatus.NOT_FOUND);
    }

	public static UserServiceException userAlreadyExists(String email) {
		return new UserServiceException("USER_ALREADY_EXISTS", "User with email '" + email + "' already exists",
				HttpStatus.CONFLICT);
	}

	public static UserServiceException invalidStatusUpdate(String userId, String reason) {
		return new UserServiceException("USER_STATUS_UPDATE_FAILED",
				"Cannot update status for user " + userId + ": " + reason, HttpStatus.BAD_REQUEST);
	}

	public static UserServiceException registrationFailed(String reason) {
		return new UserServiceException("USER_REGISTRATION_FAILED", "User registration failed: " + reason,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
