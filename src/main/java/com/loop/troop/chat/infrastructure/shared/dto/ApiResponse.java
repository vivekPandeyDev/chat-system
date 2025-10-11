package com.loop.troop.chat.infrastructure.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

	private final boolean success; // true if request succeeded

	private final String message; // human-readable message

	private final T data; // payload; null if none

}