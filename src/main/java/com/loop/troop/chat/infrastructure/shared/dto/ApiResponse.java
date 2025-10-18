package com.loop.troop.chat.infrastructure.shared.dto;

/**
 * @param success true if request succeeded
 * @param message human-readable message
 * @param data payload; null if none
 */
public record ApiResponse<T>(boolean success, String message, T data) {

}