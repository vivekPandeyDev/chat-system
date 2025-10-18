package com.loop.troop.chat.infrastructure.shared.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loop.troop.chat.domain.enums.UserStatus;

public record UserResponseDto(

		String userId, String username, String email,

		@JsonInclude(JsonInclude.Include.NON_NULL) String avatarUrl,

		UserStatus status) {
}
