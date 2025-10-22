package com.loop.troop.chat.infrastructure.shared.dto.user;

import com.loop.troop.chat.domain.enums.UserStatus;

public record UserResponseDto(

		String userId, String username, String email,

		String avatarUrl,

		UserStatus status) {
}
