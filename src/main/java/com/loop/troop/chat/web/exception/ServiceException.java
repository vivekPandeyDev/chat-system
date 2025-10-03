package com.loop.troop.chat.web.exception;

import org.springframework.http.HttpStatus;

public abstract class ServiceException extends RuntimeException {
    private final String errorCode;   // Unique error code for this type of error
    private final HttpStatus status;  // HTTP status to return
    private final String userMessage; // Friendly message for the user

    public ServiceException(String errorCode, String userMessage, HttpStatus status) {
        super(userMessage); // store user-friendly message
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
