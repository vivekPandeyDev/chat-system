package com.loop.troop.chat.shared.error.service;

public interface ErrorLogService {
    void persistError(String type, String code, String message, String detail, Exception ex, String path);
}
