package com.loop.troop.chat.infrastructure.shared.mapper;

import com.loop.troop.chat.domain.User;
import com.loop.troop.chat.infrastructure.jpa.entity.UserEntity;
import com.loop.troop.chat.infrastructure.shared.dto.user.UserResponseDto;

import java.util.Objects;
import java.util.UUID;

public class UserMapper {

	private UserMapper() {
	}

	public static User toDomain(UserEntity entity) {
		if (entity == null)
			return null;
		var user = new User(entity.getUsername(), entity.getEmail(), entity.getPassword());
		user.setImagePath(entity.getImagePath());
		user.setUserId(Objects.nonNull(entity.getUserId()) ? String.valueOf(entity.getUserId()) : null);
		user.setStatus(entity.getStatus());
		user.setPassword(entity.getPassword());
		return user;
	}

	public static UserEntity toEntity(User domain) {
		if (domain == null)
			return null;
		UUID userId = null;
		if (domain.getUserId() != null && !domain.getUserId().isEmpty()) {
			userId = UUID.fromString(domain.getUserId());
		}

		UserEntity entity = new UserEntity();
		entity.setUserId(userId);
		entity.setUsername(domain.getUsername());
		entity.setEmail(domain.getEmail());
		entity.setImagePath(domain.getImagePath());
		entity.setStatus(domain.getStatus());
		entity.setPassword(domain.getPassword());
		return entity;
	}

	public static UserResponseDto toResponseDto(User domain, String profileUrl) {
		return new UserResponseDto(domain.getUserId(), domain.getUsername(), domain.getEmail(), profileUrl, // avatarUrl
				domain.getStatus());
	}

}