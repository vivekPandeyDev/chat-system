package com.loop.troop.chat.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ServiceException extends RuntimeException {

	private final String errorCode; // Unique error code for this type of error

	private final HttpStatus status; // HTTP status to return

	private final String userMessage; // Friendly message for the user

	protected ServiceException(String errorCode, String userMessage, HttpStatus status) {
		super(userMessage); // store user-friendly message
		this.errorCode = errorCode;
		this.userMessage = userMessage;
		this.status = status;
	}

}
