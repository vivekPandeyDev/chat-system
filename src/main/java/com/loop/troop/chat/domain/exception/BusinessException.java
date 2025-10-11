package com.loop.troop.chat.domain.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends ServiceException {

	public BusinessException(String errorCode, String userMessage, HttpStatus status) {
		super(errorCode, userMessage, status);
	}

}
