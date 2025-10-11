package com.loop.troop.chat.infrastructure.shared.service;

public interface ErrorLogService {

	void persistError(String type, String code, String message, String detail, Exception ex, String path);

}
