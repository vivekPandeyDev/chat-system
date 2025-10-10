package com.loop.troop.chat.infrastructure.shared.mapper;

import com.loop.troop.chat.domain.user.User;
import com.loop.troop.chat.infrastructure.jpa.entity.UserEntity;
import com.loop.troop.chat.infrastructure.shared.dto.user.UserResponseDto;

import java.util.Objects;
import java.util.UUID;

public class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        var user = new User(entity.getUsername(), entity.getEmail(),entity.getAvatarUrl());
        user.setUserId(Objects.nonNull(entity.getUserId()) ? String.valueOf(entity.getUserId()) : null);
        user.setStatus(entity.getStatus());
        return user;
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;
        UUID userId = Objects.nonNull(domain.getUserId()) ? UUID.fromString(domain.getUserId()) : null;
        return UserEntity.builder()
                .userId(userId)
                .username(domain.getUsername())
                .email(domain.getEmail())
                .avatarUrl(domain.getAvatarUrl())
                .status(domain.getStatus())
                .build();
    }

    public static UserResponseDto toResponseDto(User domain) {
        return UserResponseDto.builder()
                .userId(domain.getUserId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .avatarUrl(domain.getAvatarUrl())
                .status(domain.getStatus())
                .build();
    }
}