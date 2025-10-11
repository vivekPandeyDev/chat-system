package com.loop.troop.chat.application.dto;

import jakarta.validation.constraints.Min;

public record PaginationQuery(@Min(value = 0) Integer page, @Min(value = 10) Integer size, String sortBy,
		String sortDir) {
}