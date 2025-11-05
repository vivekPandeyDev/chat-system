package com.loop.troop.chat.domain.constant;

import lombok.Getter;

public enum DomainConstant {

	// File-related errors
	FILE_UPLOAD_FAILED("FILE_UPLOAD_FAILED"), PRESIGNED_URL_FAILED("PRESIGNED_URL_FAILED"),
	FILE_INPUT_STREAM_FAILED("FILE_INPUT_STREAM_FAILED"), BUCKET_INIT_FAILED("BUCKET_INIT_FAILED"),

	// User-related errors
	USER_EMAIL_NOT_FOUND("USER_EMAIL_NOT_FOUND"), USER_NOT_FOUND("USER_NOT_FOUND"),
	USER_ALREADY_EXISTS("USER_ALREADY_EXISTS"), USER_STATUS_UPDATE_FAILED("USER_STATUS_UPDATE_FAILED"),
	USER_REGISTRATION_FAILED("USER_REGISTRATION_FAILED"),

	// --- Chat-related errors ---
	CHAT_ROOM_NOT_FOUND("CHAT_ROOM_NOT_FOUND"), CHAT_ROOM_FOUND("CHAT_ROOM_FOUND");

	DomainConstant(String value) {
		this.value = value;
	}

	@Getter
	final String value;

	public static DomainConstant fromValue(String value) {
		for (var code : values()) {
			if (code.value.equalsIgnoreCase(value)) {
				return code;
			}
		}
		return null;
	}

}
